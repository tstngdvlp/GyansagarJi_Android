package com.gyansagarji.android.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by New android on 25-03-2017.
 */

public class Organization extends BaseResponse {
    public Organization(String status, String return_short_message, String return_long_message) {
        super(status, return_short_message, return_long_message);
    }

    @SerializedName("result")
    ArrayList<OrganizationList> oraganizationlist;

    public ArrayList<OrganizationList> getOraganizationlist() {
        return oraganizationlist;
    }

    public void setOraganizationlist(ArrayList<OrganizationList> oraganizationlist) {
        this.oraganizationlist = oraganizationlist;
    }

    public class OrganizationList {

        @SerializedName("id")
        String id;
        @SerializedName("title")
        String title;
        @SerializedName("place")
        String place;
        @SerializedName("image")
        String image;
        @SerializedName("tiny_image")
        String tiny_image;
        @SerializedName("description")
        String description;
        @SerializedName("short_desc")
        String short_desc;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTiny_image() {
            return tiny_image;
        }

        public void setTiny_image(String tiny_image) {
            this.tiny_image = tiny_image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getShort_desc() {
            return short_desc;
        }

        public void setShort_desc(String short_desc) {
            this.short_desc = short_desc;
        }
    }
}
