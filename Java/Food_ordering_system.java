//Food ordering system-core OOP concepts such as Encapsulation, Inheritance, Abstraction, Polymorphism, Interfaces, ArrayLists, and Exception Handling while managing menu items, orders, GST, delivery charges, and reward points.

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

enum CustomerType {
    REGULAR,
    PREMIUM
}

enum OrderStatus {
    PLACED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED
}

interface BillGenerator {
    void generateBill();
}

abstract class Discount {
    public abstract double calculateDiscount(double billAmount);
}

class RegularCustomerDiscount extends Discount {

    @Override
    public double calculateDiscount(double billAmount) {
        return billAmount * 0.05;
    }
}

class PremiumCustomerDiscount extends Discount {

    @Override
    public double calculateDiscount(double billAmount) {
        return billAmount * 0.15;
    }
}

class Customer {

    private int customerId;
    private String customerName;
    private String mobileNumber;
    private int totalOrders;
    private int rewardPoints;

    public Customer(int customerId,
                    String customerName,
                    String mobileNumber,
                    int totalOrders) {

        this.customerId = customerId;
        this.customerName = customerName;
        this.mobileNumber = mobileNumber;
        this.totalOrders = totalOrders;
        this.rewardPoints = 0;
    }

    public CustomerType getCustomerType() {

        if (totalOrders > 3) {
            return CustomerType.PREMIUM;
        }

        return CustomerType.REGULAR;
    }

    public void addRewardPoints(int points) {
        rewardPoints += points;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void incrementOrders() {
        totalOrders++;
    }

    public void displayCustomer() {

        System.out.println("Customer ID   : " + customerId);
        System.out.println("Name          : " + customerName);
        System.out.println("Mobile        : " + mobileNumber);
        System.out.println("Previous Orders : " + totalOrders);
        System.out.println("Customer Type : " + getCustomerType());
    }
}

class FoodItem {

    private int itemId;
    private String itemName;
    private String category;
    private double price;

    public FoodItem(int itemId,
                    String itemName,
                    String category,
                    double price) {

        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getItemName() {
        return itemName;
    }

    public void displayItem() {

        System.out.println(
                itemId + " | " +
                itemName + " | " +
                category + " | ₹" +
                price
        );
    }
}

class Restaurant {

    private ArrayList<FoodItem> menu;

    public Restaurant() {
        menu = new ArrayList<>();
    }

    public void addFoodItem(FoodItem item) {
        menu.add(item);
    }

    public void displayMenu() {

        System.out.println("\nMENU");

        for (FoodItem item : menu) {
            item.displayItem();
        }
    }
}

class Order implements BillGenerator {

    private int orderId;
    private Customer customer;
    private ArrayList<FoodItem> items;
    private OrderStatus status;
    private LocalDateTime orderTime;

    public Order(int orderId, Customer customer) {

        this.orderId = orderId;
        this.customer = customer;

        items = new ArrayList<>();

        status = OrderStatus.PLACED;
        orderTime = LocalDateTime.now();
    }

    public void addItem(FoodItem item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "Food Item cannot be null."
            );
        }

        items.add(item);
    }

    public void addItem(FoodItem item, int quantity) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "Food Item cannot be null."
            );
        }

        for (int i = 0; i < quantity; i++) {
            items.add(item);
        }
    }

    public double calculateBill() {

        double total = 0;

        for (FoodItem item : items) {
            total += item.getPrice();
        }

        return total;
    }

    public void setOrderStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public void generateBill() {

        System.out.println("\nORDER SUMMARY\n");

        customer.displayCustomer();

        System.out.println("\nOrder ID : " + orderId);

        System.out.println("\nOrdered Items");

        for (FoodItem item : items) {
            System.out.println(
                    item.getItemName() +
                    " - ₹" +
                    item.getPrice()
            );
        }

        double subtotal = calculateBill();

        double gst = subtotal * 0.18;

        double deliveryCharge;

        if (subtotal < 500) {
            deliveryCharge = 50;
        } else {
            deliveryCharge = 0;
        }

        double billBeforeDiscount =
                subtotal + gst + deliveryCharge;

        Discount discount;

        if (customer.getCustomerType()
                == CustomerType.PREMIUM) {

            discount =
                    new PremiumCustomerDiscount();

        } else {

            discount =
                    new RegularCustomerDiscount();
        }

        double discountAmount =
                discount.calculateDiscount(
                        billBeforeDiscount
                );

        double finalBill =
                billBeforeDiscount -
                discountAmount;

        customer.addRewardPoints(
                (int) (finalBill / 100)
        );

        customer.incrementOrders();

        System.out.println("\nBill Details");

        System.out.println("Subtotal             : ₹" + subtotal);
        System.out.println("GST (18%)            : ₹" + gst);
        System.out.println("Delivery Charge      : ₹" + deliveryCharge);
        System.out.println("Bill Before Discount : ₹" + billBeforeDiscount);
        System.out.println("Discount Applied     : ₹" + discountAmount);
        System.out.println("Final Bill           : ₹" + finalBill);

        System.out.println("\nReward Points Earned : "
                + customer.getRewardPoints());

        System.out.println("Order Status         : "
                + status);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd-MM-yyyy HH:mm:ss"
                );

        System.out.println("Order Time           : "
                + orderTime.format(formatter));
    }
}

public class FoodOrderingSystem {

    public static void main(String[] args) {

        Restaurant restaurant =
                new Restaurant();

        FoodItem item1 =
                new FoodItem(
                        1,
                        "Paneer Burger",
                        "Fast Food",
                        150
                );

        FoodItem item2 =
                new FoodItem(
                        2,
                        "Veg Pizza",
                        "Fast Food",
                        300
                );

        FoodItem item3 =
                new FoodItem(
                        3,
                        "Momos",
                        "Snacks",
                        120
                );

        FoodItem item4 =
                new FoodItem(
                        4,
                        "French Fries",
                        "Snacks",
                        110
                );

        FoodItem item5 =
                new FoodItem(
                        5,
                        "Masala Dosa",
                        "South Indian",
                        180
                );

        FoodItem item6 =
                new FoodItem(
                        6,
                        "Cold Coffee",
                        "Beverage",
                        100
                );

        FoodItem item7 =
                new FoodItem(
                        7,
                        "Mango Shake",
                        "Beverage",
                        90
                );

        FoodItem item8 =
                new FoodItem(
                        8,
                        "Lemon Soda",
                        "Beverage",
                        60
                );

        FoodItem item9 =
                new FoodItem(
                        9,
                        "Masala Chai",
                        "Beverage",
                        40
                );

        FoodItem item10 =
                new FoodItem(
                        10,
                        "Fresh Lime Juice",
                        "Beverage",
                        70
                );

        restaurant.addFoodItem(item1);
        restaurant.addFoodItem(item2);
        restaurant.addFoodItem(item3);
        restaurant.addFoodItem(item4);
        restaurant.addFoodItem(item5);
        restaurant.addFoodItem(item6);
        restaurant.addFoodItem(item7);
        restaurant.addFoodItem(item8);
        restaurant.addFoodItem(item9);
        restaurant.addFoodItem(item10);

        restaurant.displayMenu();

        Customer customer =
                new Customer(
                        101,
                        "Rahul Sharma",
                        "9876543210",
                        5
                );

        Order order =
                new Order(
                        1001,
                        customer
                );

        order.addItem(item1);
        order.addItem(item2);
        order.addItem(item6);
        order.addItem(item8);

        order.setOrderStatus(
                OrderStatus.DELIVERED
        );

        order.generateBill();
    }
}
