package com.code.ecommercebackend.services.cart;

import com.code.ecommercebackend.dtos.request.cart.AddToCartRequest;
import com.code.ecommercebackend.dtos.request.cart.UpdateCartRequest;
import com.code.ecommercebackend.dtos.response.cart.ProductCartResponse;
import com.code.ecommercebackend.dtos.response.variant.VariantResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.repositories.CartRepository;
import com.code.ecommercebackend.repositories.PromotionRepository;
import com.code.ecommercebackend.repositories.VariantRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.services.variant.VariantServiceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImpl extends BaseServiceImpl<Cart, String> implements CartService {
    private final CartRepository cartRepository;
    private final PromotionRepository promotionRepository;
    private final VariantServiceImpl variantServiceImpl;
    private final VariantRepository variantRepository;

    public CartServiceImpl(MongoRepository<Cart, String> repository,
                           CartRepository cartRepository,
                           PromotionRepository promotionRepository,
                           VariantServiceImpl variantServiceImpl, VariantRepository variantRepository) {
        super(repository);
        this.cartRepository = cartRepository;
        this.promotionRepository = promotionRepository;
        this.variantServiceImpl = variantServiceImpl;
        this.variantRepository = variantRepository;
    }

    @Override
    public Cart addToCart(AddToCartRequest request) {
        Optional<Cart> opCart = cartRepository.findByUserId(request.getUserId());
        Cart cart;
        if (opCart.isPresent()) {
            cart = opCart.get();
            List<ProductCart> productCarts = cart.getProductCarts();
            ProductCart productCart = request.getProductCart();
            boolean update = false;
            for (int i = 0; i < productCarts.size(); i++) {
                ProductCart oldProductCart = productCarts.get(i);
                if (oldProductCart.getVariantId().equals(productCart.getVariantId())) {
                    oldProductCart.setQuantity(oldProductCart.getQuantity() + productCart.getQuantity());
                    productCarts.set(i, oldProductCart);
                    update = true;
                }
            }
            if (!update) {
                productCarts.add(productCart);
            }
            cart.setProductCarts(productCarts);
        } else {
            cart = new Cart();
            cart.setUserId(request.getUserId());
            cart.setProductCarts(List.of(request.getProductCart()));
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateCart(UpdateCartRequest request) throws DataNotFoundException {
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cart not found"));
        List<ProductCart> productCarts = cart.getProductCarts();
        productCarts.set(request.getIndex(), request.getProductUpdate());
        cart.setProductCarts(productCarts);
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCartItem(String userId, String itemId) throws DataNotFoundException {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("cart not found"));
        List<ProductCart> productCarts = cart.getProductCarts();
        List<ProductCart> newProductCarts = productCarts.stream().filter(c -> !c.getVariantId().equals(itemId)).toList();
        cart.setProductCarts(newProductCarts);
        cartRepository.save(cart);
    }

    @Override
    public List<ProductCartResponse> getCartByUserId(String userId) {
        Optional<Cart> opCart = cartRepository.findByUserId(userId);
        if (opCart.isPresent()) {
            List<ProductCart> productCarts = opCart.get().getProductCarts();
            List<String> variantIds = productCarts.stream().map(ProductCart::getVariantId).
                    filter(Objects::nonNull).
                    toList();
            return mapToProductCartResponse(variantIds, productCarts);
        }
        return new ArrayList<>();
    }

    public List<ProductCartResponse> mapToProductCartResponse(List<String> variantIds, List<ProductCart> productCarts) {
        List<Variant> variants = variantRepository.findAllById(variantIds);
        variants.sort(Comparator.comparing(v -> variantIds.indexOf(v.getId())));
        List<ProductCartResponse> productCartResponses = new ArrayList<>();
        for(int i = 0; i < variants.size(); i++) {
            VariantResponse variant = variantServiceImpl.mapToVariantResponse(variants.get(i));
            Product product = variant.getProduct();
            List<Promotion> promotions = promotionRepository.findFirstByCurrentDateAndProductId(product.getId(),
                    Sort.by(Sort.Direction.DESC, "startDate"));

            ProductCartResponse productCartResponse = new ProductCartResponse();
            productCartResponse.setVariantResponse(variant);
            productCartResponse.setQuantity(productCarts.get(i).getQuantity());
            if(!promotions.isEmpty()) {
                productCartResponse.setPromotion(promotions.get(0));
            }
            productCartResponses.add(productCartResponse);
        }

        return productCartResponses;
    }
}
