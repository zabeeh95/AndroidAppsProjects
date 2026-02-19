package com.example.carddesign;

/**
 * Data model for each row of the RecyclerView.
 */
class Sport {
    //Member variables representing the title and information about the sport
    private String title;
    private String info;
        private final int imageResource;

    public Sport(String title, String info, int imageResource) {
        this.title = title;
        this.info = info;
        this.imageResource = imageResource;
    }

    public int getImageResource() {
        return imageResource;
    }

    /**
     * Gets the title of the sport
     * @return The title of the sport.
     */
    String getTitle() {
        return title;
    }
    /**
     * Gets the info about the sport
     * @return The info about the sport.
     */
    String getInfo() {
        return info;
    }
}

