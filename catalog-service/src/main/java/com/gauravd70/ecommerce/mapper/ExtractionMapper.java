package com.gauravd70.ecommerce.mapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gauravd70.ecommerce.dtos.intermediates.ExtractedProduct;
import com.gauravd70.ecommerce.dtos.intermediates.NormalizedProduct;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ExtractionMapper {
     public String toSHA26(String id) {
        if(id == null) {
            return null;
        }
        
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-26");
            
            byte[] idBytes = id.getBytes(StandardCharsets.UTF_8);

            byte[] idHashBytes = messageDigest.digest(idBytes);

            StringBuilder builder = new StringBuilder();
            
            for(byte idHashByte : idHashBytes) {
                builder.append(String.format("%02x", idHashByte));
            }

            return builder.toString();
        } catch(NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not found", e);
        }

        return null;
    }

    public String toCanonicalIdRepresentation(Map<String, String> map) {
        if(map == null) {
            return null;
        }

        String[][] pairs = new String[map.size()][2];

        int i = 0;

        for(Map.Entry<String, String> entry : map.entrySet()) {
            pairs[i++] = new String[]{entry.getKey(), entry.getValue()};
        }

        Arrays.sort(pairs, (a, b) -> a[0].compareTo(b[0]));

        StringBuilder builder = new StringBuilder();

        for(String[] pair : pairs) {
            builder.append(pair[0]).append("=").append(pair[1]).append("|");
        }

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    public Map<String, String> extractByVariant(Map<String, String> attributes, List<String> variantAttributes) {
        if(variantAttributes == null || attributes == null) {
            return Collections.emptyMap();
        }

        Map<String, String> resultMap = new HashMap<>();

        for(String variant : variantAttributes) {
            if(attributes.containsKey(variant)) {
                resultMap.put(variant, attributes.get(variant));
            }
        }

        return resultMap;
    }

    public Map<String, String> extractByFamily(NormalizedProduct normalizedProduct) {
        if(normalizedProduct == null) {
            return Collections.emptyMap();
        }

        Map<String, String> familyAttributes = new HashMap<>();

        if(normalizedProduct.getBrand() == null) {
            familyAttributes.put("brand", normalizedProduct.getBrand());
        }

        if(normalizedProduct.getModel() == null) {
            familyAttributes.put("model", normalizedProduct.getModel());
        }

        return familyAttributes;
    }

    public String toSentenceCase(String... strings) {
        if(strings == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        for(String string : strings) {
            char[] chars = string.toCharArray();

            if(chars.length > 0) {
                chars[0] = Character.toUpperCase(chars[0]);
            }

            builder.append(new String(chars)).append(" ");
        }

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    public ExtractedProduct extract(NormalizedProduct normalizedProduct, ProductActionsMessage productActionsMessage) {
        if(normalizedProduct == null || productActionsMessage == null || productActionsMessage.getCategory() == null) {
            return null;
        }

        ExtractedProduct extractedProduct = ExtractedProduct.builder()
            .name(toSentenceCase(normalizedProduct.getBrand(), normalizedProduct.getModel()))
            .productId(productActionsMessage.getId())
            .categoryId(productActionsMessage.getCategory().getId())
            .familyAttributes(extractByFamily(normalizedProduct))
            .variantAttributes(extractByVariant(normalizedProduct.getAttributes(), productActionsMessage.getCategory().getVariantAttributes()))
            .build();

        String familyIdRepresentation = toCanonicalIdRepresentation(extractedProduct.getFamilyAttributes());
        String variantIdRepresentation = toCanonicalIdRepresentation(extractedProduct.getVariantAttributes());

        return extractedProduct.toBuilder()
            .familyIdRepresentation(familyIdRepresentation)
            .variantIdRepresentation(variantIdRepresentation)
            .familyId(toSHA26(familyIdRepresentation))
            .variantId(toSHA26(variantIdRepresentation))
            .build();
    } 
}
