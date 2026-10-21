
package com.frankie;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WeaponApiClient {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Weapon> searchWeapons(String searchTerm)
            throws Exception {

        String encodedTerm = URLEncoder.encode(
                searchTerm, StandardCharsets.UTF_8);

        String apiUrl =
                "https://blocksitems.com/api/v1/items?search="
                        + encodedTerm;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "API request failed with status "
                            + response.statusCode());
        }

        JsonNode root = mapper.readTree(response.body());
        List<Weapon> weapons = new ArrayList<>();

        // The API may return either a JSON array or an object
        // containing an array. Handle both structures.
        JsonNode items = root.isArray()
                ? root
                : root.has("items") ? root.get("items")
                : root.has("data") ? root.get("data")
                : null;

        if (items == null || !items.isArray()) {
            throw new IllegalStateException(
                    "Could not find an item array in the API response. "
                            + "Inspect the JSON response to confirm its structure.");
        }

        for (JsonNode item : items) {
            String name = item.path("display_name").asText("");

            if (!item.path("max_damage").canConvertToInt()
                    || !item.path("enchantment_value").canConvertToInt()) {
                continue;
            }

            int durability = item.path("max_damage").asInt();
            int enchantability =
                    item.path("enchantment_value").asInt();

            if (!name.isBlank()
                    && item.path("is_damageable").asBoolean(false)) {
                weapons.add(new Weapon(
                        name, durability, enchantability));
            }
        }

        return weapons;
    }
}
