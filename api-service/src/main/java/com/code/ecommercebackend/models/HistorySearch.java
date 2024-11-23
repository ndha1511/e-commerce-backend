package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "history_search")
public class HistorySearch extends BaseModel {
    @Field(name = "user_id")
    private String userId;
    private String content;
    @Field(name = "search_content")
    private List<String> searchContent;

    public void normalizerName(String content) {
        List<String> namesNormalize = new ArrayList<>();
        namesNormalize.add(content.toLowerCase());
        String temp = Normalizer.normalize(content, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String normalize = pattern.matcher(temp).replaceAll("").toLowerCase();
        namesNormalize.add(normalize);
        this.searchContent = namesNormalize;

    }

}
