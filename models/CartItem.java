package models;

/**
 * Represents an item in the shopping cart, containing the medicine and its quantity.
 */
public class CartItem {
    private Medicine medicine; // The medicine object
    private int quantity; // The quantity of the medicine

    /**
     * Initializes a new CartItem object.
     * 
     * @param medicine The medicine object to be added to the cart.
     * @param quantity The quantity of the medicine to be added.
     */
    public CartItem(Medicine medicine, int quantity) {
        this.medicine = medicine;
        this.quantity = quantity;
    }

    /**
     * Returns the medicine object.
     * 
     * @return The medicine object.
     */
    public Medicine getMedicine() {
        return medicine;
    }

    /**
     * Sets the medicine object.
     * 
     * @param medicine The medicine object to be set.
     */
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    /**
     * Returns the quantity of the medicine.
     * 
     * @return The quantity of the medicine.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the medicine.
     * 
     * @param quantity The quantity to be set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates and returns the total price of the medicine.
     * 
     * @return The total price of the medicine.
     */
    public double getTotalPrice() {
        return medicine.getPrice() * quantity;
    }

    /**
     * Provides a string representation of the cart item.
     * 
     * @return A string representation of the cart item.
     */
    @Override
    public String toString() {
        return medicine.getName() + " x" + quantity + " - Php" + getTotalPrice();
    }
}
