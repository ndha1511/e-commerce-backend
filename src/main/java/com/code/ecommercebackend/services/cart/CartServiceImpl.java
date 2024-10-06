package com.code.ecommercebackend.services.cart;

import com.code.ecommercebackend.dtos.request.cart.AddToCartRequest;
import com.code.ecommercebackend.dtos.request.cart.UpdateCartRequest;
import com.code.ecommercebackend.dtos.response.cart.ProductCartResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.repositories.CartRepository;
import com.code.ecommercebackend.repositories.InventoryRepository;
import com.code.ecommercebackend.repositories.PromotionRepository;
import com.code.ecommercebackend.repositories.VariantRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartServiceImpl extends BaseServiceImpl<Cart, String> implements CartService {
    private final CartRepository cartRepository;
    private final VariantRepository variantRepository;
    private final InventoryRepository inventoryRepository;
    private final PromotionRepository promotionRepository;

    public CartServiceImpl(MongoRepository<Cart, String> repository,
                           CartRepository cartRepository,
                           VariantRepository variantRepository, InventoryRepository inventoryRepository, PromotionRepository promotionRepository) {
        super(repository);
        this.cartRepository = cartRepository;
        this.variantRepository = variantRepository;
        this.inventoryRepository = inventoryRepository;
        this.promotionRepository = promotionRepository;
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
    public List<ProductCartResponse> getCartByUserId(String userId) {
        Optional<Cart> opCart = cartRepository.findByUserId(userId);
        if (opCart.isPresent()) {
            List<ProductCart> productCarts = opCart.get().getProductCarts();
            List<String> variantIds = productCarts.stream().map(ProductCart::getVariantId).
                    filter(Objects::nonNull).
                    toList();
            List<Variant> variants = variantRepository.findAllById(variantIds);
            return mapToProductCartResponse(variants, productCarts);
        }
        return new ArrayList<>();
    }

    public List<ProductCartResponse> mapToProductCartResponse(List<Variant> variants, List<ProductCart> productCarts) {
        List<ProductCartResponse> productCartResponses = new ArrayList<>();
        for(int i = 0; i < variants.size(); i++) {
            Variant variant = variants.get(i);
            Product product = variant.getProduct();
            List<Inventory> inventories = inventoryRepository.findByVariantId(variant.getId());
            int quantityInStock = inventories.stream()
                    .mapToInt(Inventory::getImportQuantity).sum();
            Optional<Promotion> opPromotion = promotionRepository.findFirstByCurrentDateAndProductId(product.getId());
            ProductCartResponse productCartResponse = new ProductCartResponse();
            productCartResponse.setVariant(variant);
            productCartResponse.setQuantity(productCarts.get(i).getQuantity());
            productCartResponse.setQuantityInStock(quantityInStock);
            opPromotion.ifPresent(productCartResponse::setPromotion);
            productCartResponses.add(productCartResponse);
        }

        return productCartResponses;
    }
}
