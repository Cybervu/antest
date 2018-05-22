package com.home.apisdk.apiModel;

import java.util.ArrayList;

public class ContentPriceDetailsOutput {
    String code;
    String status;
    ArrayList<contentPrice> ContentPrice = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ArrayList<contentPrice> getContentPrice() {
        return ContentPrice;
    }

    public void setContentPrice(ArrayList<contentPrice> contentPrice) {
        ContentPrice = contentPrice;
    }


    public class contentPrice {
        String voucher;
        ArrayList<ppv> PPV = new ArrayList<>();

        public String getVoucher() {
            return voucher;
        }

        public void setVoucher(String voucher) {
            this.voucher = voucher;
        }

        public ArrayList<ppv> getPPV() {
            return PPV;
        }

        public void setPPV(ArrayList<ppv> PPV) {
            this.PPV = PPV;
        }

        public class ppv {
            String subscriber_price;
            String nonsubscriber_price;

            public String getSubscriber_price() {
                return subscriber_price;
            }

            public void setSubscriber_price(String subscriber_price) {
                this.subscriber_price = subscriber_price;
            }

            public String getNonsubscriber_price() {
                return nonsubscriber_price;
            }

            public void setNonsubscriber_price(String nonsubscriber_price) {
                this.nonsubscriber_price = nonsubscriber_price;
            }


            ArrayList<show> Show = new ArrayList<>();
            ArrayList<season> Season = new ArrayList<>();
            ArrayList<episode> Episode = new ArrayList<>();

            public ArrayList<show> getShow() {
                return Show;
            }

            public void setShow(ArrayList<show> show) {
                Show = show;
            }

            public ArrayList<season> getSeason() {
                return Season;
            }

            public void setSeason(ArrayList<season> season) {
                Season = season;
            }

            public ArrayList<episode> getEpisode() {
                return Episode;
            }

            public void setEpisode(ArrayList<episode> episode) {
                Episode = episode;
            }


            public class show {
                String subscriber_price;
                String nonsubscriber_price;

                public String getSubscriber_price() {
                    return subscriber_price;
                }

                public void setSubscriber_price(String subscriber_price) {
                    this.subscriber_price = subscriber_price;
                }

                public String getNonsubscriber_price() {
                    return nonsubscriber_price;
                }

                public void setNonsubscriber_price(String nonsubscriber_price) {
                    this.nonsubscriber_price = nonsubscriber_price;
                }

            }

            public class season {

                ArrayList<defaultPrice> DefaultPrice = new ArrayList<>();
                ArrayList<seasonalPrice> SeasonalPrice = new ArrayList<>();

                public ArrayList<defaultPrice> getDefaultPrice() {
                    return DefaultPrice;
                }

                public void setDefaultPrice(ArrayList<defaultPrice> defaultPrice) {
                    DefaultPrice = defaultPrice;
                }

                public ArrayList<seasonalPrice> getSeasonalPrice() {
                    return SeasonalPrice;
                }

                public void setSeasonalPrice(ArrayList<seasonalPrice> seasonalPrice) {
                    SeasonalPrice = seasonalPrice;
                }


                public class defaultPrice {
                    String subscriber_price;
                    String nonsubscriber_price;

                    public String getSubscriber_price() {
                        return subscriber_price;
                    }

                    public void setSubscriber_price(String subscriber_price) {
                        this.subscriber_price = subscriber_price;
                    }

                    public String getNonsubscriber_price() {
                        return nonsubscriber_price;
                    }

                    public void setNonsubscriber_price(String nonsubscriber_price) {
                        this.nonsubscriber_price = nonsubscriber_price;
                    }

                }

                public class seasonalPrice {

                    String subscriber_price;
                    String nonsubscriber_price;
                    String season_id;

                    public String getSeason_id() {
                        return season_id;
                    }

                    public void setSeason_id(String season_id) {
                        this.season_id = season_id;
                    }

                    public String getSubscriber_price() {
                        return subscriber_price;
                    }

                    public void setSubscriber_price(String subscriber_price) {
                        this.subscriber_price = subscriber_price;
                    }

                    public String getNonsubscriber_price() {
                        return nonsubscriber_price;
                    }

                    public void setNonsubscriber_price(String nonsubscriber_price) {
                        this.nonsubscriber_price = nonsubscriber_price;
                    }

                }
            }

            public class episode {

                String subscriber_price;
                String nonsubscriber_price;

                public String getSubscriber_price() {
                    return subscriber_price;
                }

                public void setSubscriber_price(String subscriber_price) {
                    this.subscriber_price = subscriber_price;
                }

                public String getNonsubscriber_price() {
                    return nonsubscriber_price;
                }

                public void setNonsubscriber_price(String nonsubscriber_price) {
                    this.nonsubscriber_price = nonsubscriber_price;
                }

            }

        }
    }


}
