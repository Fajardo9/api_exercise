package com.globant.api_tae.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Resource.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private String name;
    private String trademark;
    private String stock;
    private String price;
    private String description;
    private String tags;
    private String active;
    private String id;

}
