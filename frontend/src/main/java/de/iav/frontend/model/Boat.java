package de.iav.frontend.model;

    public record Boat(

            String boatName,
            String boatType
    ) {
        public String getBoatName() {
            return this.boatName;
        }

        public String getBoatType() {
            return this.boatType;
        }



}
