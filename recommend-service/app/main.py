import pandas as pd
from surprise import Dataset, Reader, SVD
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
import pymongo
import numpy as np
from flask import Flask, jsonify, request



def get_content_based_recommendations(product_id, top_n, content_df, content_similarity):
    index = content_df[content_df['product_id'] == product_id].index[0]
    similarity_scores = content_similarity[index]
    similar_indices = similarity_scores.argsort()[::-1][1:top_n + 1]
    recommendations = content_df.loc[similar_indices, 'product_id'].values
    return recommendations

def get_collaborative_filtering_recommendations(user_id, top_n, trainset, algo):
    testset = trainset.build_anti_testset()
    testset = filter(lambda x: x[0] == user_id, testset)
    predictions = algo.test(testset)
    predictions.sort(key=lambda x: x.est, reverse=True)
    recommendations = [prediction.iid for prediction in predictions[:top_n]]
    return recommendations

def get_hybrid_recommendations(user_id, product_id, top_n, content_df, content_similarity, trainset, algo):
    content_based_recommendations = get_content_based_recommendations(product_id, top_n, content_df, content_similarity)
    collaborative_filtering_recommendations = get_collaborative_filtering_recommendations(user_id, top_n, trainset, algo)
    hybrid_recommendations = list(set(content_based_recommendations + collaborative_filtering_recommendations))
    return hybrid_recommendations[:top_n]

def get_data(user_id, product_id, top_n = 10):
    mongoClient = pymongo.MongoClient(
        "mongodb+srv://ndha1511:B1x5UuzC0BVEioWu@cluster0.4cyuq.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")

    database = mongoClient["e-commerce-db"]

    collection = database["product_features"]

    documents = collection.find()

    data = pd.DataFrame(data=documents, columns=['user_id', 'product_id', 'product_name', 'brand', 'category', 'price', 'rating'])
    # data = pd.read_csv("../fashion_products.csv")
    # print(data.head())

    content_df = data[['product_id', 'product_name', 'brand',
                       'category', 'price']]

    content_df = content_df.drop_duplicates()

    content_df['Content'] = content_df.apply(lambda row: ' '.join(row.dropna().astype(str)), axis=1)

    # Use TF-IDF vectorizer to convert content into a matrix of TF-IDF features
    tfidf_vectorizer = TfidfVectorizer()
    content_matrix = tfidf_vectorizer.fit_transform(content_df['Content'])

    content_similarity = linear_kernel(content_matrix, content_matrix)

    reader = Reader(rating_scale=(1, 5))
    data = Dataset.load_from_df(data[['user_id',
                                      'product_id',
                                      'rating']], reader)
    algo = SVD()
    trainset = data.build_full_trainset()
    algo.fit(trainset)

    if user_id == 0 and product_id != 0:
        recommendations = get_content_based_recommendations(product_id, top_n, content_df, content_similarity)
        return recommendations
    if product_id == 0 and user_id != 0:
        recommendations = get_collaborative_filtering_recommendations(user_id, top_n, trainset, algo)
        return np.array(recommendations, dtype=np.int64)
    recommendations = get_hybrid_recommendations(user_id, product_id, top_n, content_df, content_similarity, trainset, algo)
    return np.array(recommendations, dtype=np.int64)


controller = Flask(__name__)
@controller.route('/recommend', methods=['GET'])
def recommend_products():
    user_id = request.args.get('user_id', type=int, default=0)
    product_id = request.args.get('product_id', type=int, default=0)
    top_n = request.args.get('top_n', type=int, default=10)
    recommendations = get_data(user_id, product_id, top_n)
    return jsonify(recommendations.tolist())

def main():
    controller.run(host='0.0.0.0', port=3000, debug=True)

if __name__ == "__main__":
    main()


