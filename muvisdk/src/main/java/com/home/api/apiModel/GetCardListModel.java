package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 2/9/2018.
 *
 * @author Abhishek
 */

public class GetCardListModel {

    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("cards")
    @Expose
    private ArrayList<CardDetails> cards = null;

    public ArrayList<CardDetails> getCards() {
        return cards;
    }

    public void setCards(ArrayList<CardDetails> cards) {
        this.cards = cards;
    }

    public String getCanSaveCard() {
        return canSaveCard;
    }

    public void setCanSaveCard(String canSaveCard) {
        this.canSaveCard = canSaveCard;
    }

    @SerializedName("can_save_card")
    @Expose
    private String canSaveCard = "";

    /**
     * This method is used to get the message
     *
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * This method is used to set the message
     *
     * @param msg For setting the message
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * This method is used to get the server code
     *
     * @return code
     */

    public Integer getCode() {
        return code;
    }

    /**
     * This method is used to set the server code
     *
     * @param code For setting the server code
     */

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * This method is used to get the status
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method is used to set the status
     *
     * @param status For setting the status
     */

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This method is used to get the items
     *
     * @return items
     */

   /* public Items getItems() {
        return items;
    }

    *//**
     * This method is used to set the items
     *
     * @param items For setting the items
     *//*

    public void setItems(Items items) {
        this.items = items;
    }

    public class Items {

        @SerializedName("can_save_card")
        @Expose
        private Integer canSaveCard = 0;
        @SerializedName("cards")
        @Expose
        private ArrayList<CardDetails> cards = null;

        *//**
         * This method is used to get the can save card details
         *
         * @return canSaveCard
         *//*

        public Integer getCanSaveCard() {
            return canSaveCard;
        }

        *//**
         * This method is used to set the can save card details
         *
         * @param canSaveCard For setting the card details whether to save or not
         *//*

        public void setCanSaveCard(Integer canSaveCard) {
            this.canSaveCard = canSaveCard;
        }

        *//**
         * This method is used to get the array list of cards
         *
         * @return cards
         *//*
        public ArrayList<CardDetails> getCards() {
            return cards;
        }

        *//**
         * This method is used to set the array list of cards
         *
         * @param cards For setting the cards
         *//*
        public void setCards(ArrayList<CardDetails> cards) {
            this.cards = cards;
        }

    }*/

    public class CardDetails {

        public String getCard_last_fourdigit() {
            return card_last_fourdigit;
        }

        public void setCard_last_fourdigit(String card_last_fourdigit) {
            this.card_last_fourdigit = card_last_fourdigit;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        @SerializedName("card_last_fourdigit")
        @Expose
        private String card_last_fourdigit;
        @SerializedName("card_id")
        @Expose
        private String card_id;

    }
}
