package prgms;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class Ecommerce extends JFrame {

    Product[][][] products = new Product[4][4][10];

    Product[] cartItems = new Product[500];
    int[] cartQty = new int[500];
    int cartCount = 0;

    JTable productTable;
    DefaultTableModel productModel;

    JPanel subPanel;

    String[] categories = {
            "🔵 Electronics",
            "🟣 Fashion",
            "🟡 Home Decor",
            "🟢 Household"
    };

    String[][] subCategories = {
            {"Mobiles", "Laptops", "Headphones", "Powerbanks"},
            {"Men Wear", "Women Wear", "Kids Wear", "Footwear"},
            {"Wall Decor", "Lighting", "Plants", "Showpieces"},
            {"Kitchen", "Cleaning", "Laundry", "Storage"}
    };

    public Ecommerce() {

        setTitle("E-Commerce Application");
        setSize(1250, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadProducts();

        // TOP PANEL
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton checkoutBtn = new JButton("💳 Checkout");
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutBtn.setBackground(new Color(0, 150, 136));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.addActionListener(e -> openCheckoutWindow());
        top.add(checkoutBtn);
        add(top, BorderLayout.NORTH);

        // LEFT CATEGORIES PANEL
        JPanel left = new JPanel(new GridLayout(4,1,10,10));
        left.setBorder(BorderFactory.createTitledBorder("Categories"));

        for (int i = 0; i < 4; i++) {
            JButton b = new JButton(categories[i]);
            b.setFont(new Font("Arial", Font.BOLD, 18));
            int idx = i;
            b.addActionListener(e -> showSubCategories(idx));
            left.add(b);
        }
        add(left, BorderLayout.WEST);

        // SUBCATEGORY PANEL
        subPanel = new JPanel(new GridLayout(4,1,10,10));
        subPanel.setBorder(BorderFactory.createTitledBorder("Subcategories"));
        add(subPanel, BorderLayout.CENTER);

        // PRODUCT TABLE
        productModel = new DefaultTableModel(
                new String[]{"Model", "Brand", "Price", "Add"}, 0
        );

        productTable = new JTable(productModel);
        productTable.setRowHeight(30);

        productTable.getColumn("Add").setCellRenderer(new ButtonRenderer());
        productTable.getColumn("Add").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        JScrollPane scroll = new JScrollPane(productTable);

        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Products"));
        right.add(scroll);
        add(right, BorderLayout.EAST);
    }

    // SHOW SUBCATEGORIES
    void showSubCategories(int c) {
        subPanel.removeAll();
        for (int i = 0; i < 4; i++) {
            JButton b = new JButton(subCategories[c][i]);
            b.setFont(new Font("Arial", Font.BOLD, 18));
            int sub = i;
            b.addActionListener(e -> showProducts(c, sub));
            subPanel.add(b);
        }
        subPanel.revalidate();
        subPanel.repaint();
    }

    // SHOW PRODUCTS
    void showProducts(int c, int s) {
        productModel.setRowCount(0);
        for (int i = 0; i < 10; i++) {
            Product p = products[c][s][i];
            productModel.addRow(new Object[]{p.name, p.brand, "₹" + p.price, "Add"});
        }
    }

    // ADD TO CART
    public void addFromTable(int row) {
        String model = productTable.getValueAt(row, 0).toString();
        Product p = findProduct(model);
        if (p == null) return;

        String qty = JOptionPane.showInputDialog(this, "Enter Quantity:", "1");
        if (qty == null) return;

        int q = Integer.parseInt(qty);

        cartItems[cartCount] = p;
        cartQty[cartCount] = q;
        cartCount++;

        JOptionPane.showMessageDialog(this,
                "Added to Cart:\n" + p.name +
                        "\nQuantity: " + q +
                        "\nTotal: ₹" + (p.price * q),
                "Added", JOptionPane.INFORMATION_MESSAGE);
    }

    Product findProduct(String name) {
        for (int c = 0; c < 4; c++) {
            for (int s = 0; s < 4; s++) {
                for (int i = 0; i < 10; i++) {
                    if (products[c][s][i].name.equals(name))
                        return products[c][s][i];
                }
            }
        }
        return null;
    }

    // CHECKOUT WINDOW
    void openCheckoutWindow() {

        if (cartCount == 0) {
            JOptionPane.showMessageDialog(this,
                    "Cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame cf = new JFrame("Checkout");
        cf.setSize(600, 500);
        cf.setLocationRelativeTo(this);

        DefaultTableModel cm = new DefaultTableModel(
                new String[]{"Product", "Qty", "Price", "Subtotal"}, 0
        );

        JTable ct = new JTable(cm);
        ct.setRowHeight(28);

        double total = 0;

        for (int i = 0; i < cartCount; i++) {
            Product p = cartItems[i];
            int q = cartQty[i];
            double s = p.price * q;

            total += s;

            cm.addRow(new Object[]{p.name, q, p.price, s});
        }

        JLabel totalLbl = new JLabel("Total: ₹" + total);
        totalLbl.setFont(new Font("Arial", Font.BOLD, 18));

        JButton payBtn = new JButton("Proceed to Payment");
        payBtn.setBackground(new Color(33, 150, 243));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFont(new Font("Arial", Font.BOLD, 16));

        double finalAmount = total;

        payBtn.addActionListener(e -> paymentWindow(finalAmount));

        JPanel bottom = new JPanel();
        bottom.add(totalLbl);
        bottom.add(payBtn);

        cf.add(new JScrollPane(ct));
        cf.add(bottom, BorderLayout.SOUTH);

        cf.setVisible(true);
    }

    // PAYMENT WINDOW
    void paymentWindow(double total) {

        JFrame pf = new JFrame("Payment");
        pf.setSize(400, 300);
        pf.setLocationRelativeTo(this);
        pf.setLayout(new GridLayout(6, 1));

        JLabel label = new JLabel("Select Payment Method", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        JRadioButton upi = new JRadioButton("UPI");
        JRadioButton nb = new JRadioButton("Net Banking");
        ButtonGroup bg = new ButtonGroup();
        bg.add(upi);
        bg.add(nb);

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        JButton pay = new JButton("Pay ₹" + total);
        pay.setBackground(new Color(0, 200, 83));
        pay.setForeground(Color.WHITE);
        pay.setFont(new Font("Arial", Font.BOLD, 16));

        pay.addActionListener(e -> {
            JOptionPane.showMessageDialog(pf,
                    " PAYMENT SUCCESSFUL!\n ORDER CONFIRMED!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            cartCount = 0; // clear cart
            pf.dispose();
        });

        pf.add(label);
        pf.add(upi);
        pf.add(nb);
        pf.add(new JLabel("Enter ID / Username:"));
        pf.add(user);
        pf.add(pay);

        pf.setVisible(true);
    }

    // LOAD PRODUCTS (All 160)
    void loadProducts() {

        products[0][0] = new Product[]{
                new Product("Samsung S21", "Samsung", "Black", "8GB", "128GB", 52000),
                new Product("iPhone 13", "Apple", "Blue", "4GB", "128GB", 61000),
                new Product("Redmi Note 10", "Xiaomi", "White", "6GB", "128GB", 15000),
                new Product("Vivo Y21", "Vivo", "Black", "4GB", "64GB", 14000),
                new Product("Realme 8", "Realme", "Silver", "8GB", "128GB", 18000),
                new Product("Oppo F19", "Oppo", "Blue", "6GB", "128GB", 19000),
                new Product("Samsung M31", "Samsung", "Blue", "6GB", "128GB", 16000),
                new Product("OnePlus Nord", "OnePlus", "Grey", "8GB", "128GB", 28000),
                new Product("iQOO Z6", "iQOO", "Black", "6GB", "128GB", 17000),
                new Product("Pixel 6", "Google", "Black", "8GB", "128GB", 49000)
        };

        products[0][1] = new Product[]{
                new Product("HP Pavilion", "HP", "Silver", "8GB", "512GB SSD", 56000),
                new Product("Dell Inspiron", "Dell", "Black", "8GB", "512GB SSD", 58000),
                new Product("Lenovo ThinkPad", "Lenovo", "Black", "16GB", "512GB SSD", 72000),
                new Product("MacBook Air M1", "Apple", "Grey", "8GB", "256GB SSD", 85000),
                new Product("Acer Aspire 7", "Acer", "Black", "8GB", "512GB SSD", 50000),
                new Product("Asus VivoBook", "Asus", "Silver", "8GB", "256GB SSD", 48000),
                new Product("MSI GF63", "MSI", "Black", "8GB", "512GB SSD", 67000),
                new Product("HP Victus", "HP", "Blue", "16GB", "512GB SSD", 78000),
                new Product("Dell XPS", "Dell", "White", "16GB", "512GB SSD", 98000),
                new Product("MacBook Pro M2", "Apple", "Grey", "16GB", "512GB SSD", 140000)
        };

        products[0][2] = new Product[]{
                new Product("Boat 135", "Boat", "Black", "-", "-", 499),
                new Product("Sony XM4", "Sony", "Black", "-", "-", 24999),
                new Product("JBL 500BT", "JBL", "Blue", "-", "-", 3500),
                new Product("AirPods", "Apple", "White", "-", "-", 15000),
                new Product("Realme Buds", "Realme", "Black", "-", "-", 799),
                new Product("Noise One", "Noise", "Grey", "-", "-", 1699),
                new Product("Skullcandy", "Skullcandy", "Black", "-", "-", 2800),
                new Product("Boat Airdopes", "Boat", "White", "-", "-", 1999),
                new Product("Samsung Buds", "Samsung", "Black", "-", "-", 6999),
                new Product("OnePlus Buds", "OnePlus", "Blue", "-", "-", 4999)
        };

        products[0][3] = new Product[]{
                new Product("Mi PB", "Mi", "Black", "-", "-", 999),
                new Product("Realme PB", "Realme", "Yellow", "-", "-", 1899),
                new Product("Samsung PB", "Samsung", "Blue", "-", "-", 1600),
                new Product("Ambrane PB", "Ambrane", "Black", "-", "-", 1200),
                new Product("Boat PB", "Boat", "White", "-", "-", 999),
                new Product("OnePlus PB", "OnePlus", "Black", "-", "-", 1500),
                new Product("Anker PB", "Anker", "Black", "-", "-", 2200),
                new Product("Lenovo PB", "Lenovo", "Black", "-", "-", 1100),
                new Product("PTron PB", "PTron", "Blue", "-", "-", 899),
                new Product("Intex PB", "Intex", "Black", "-", "-", 799)
        };

        // Fashion
        products[1][0] = new Product[]{
                new Product("Men Shirt", "Roadster", "Blue", "-", "M", 799),
                new Product("Men Jeans", "Levis", "Black", "-", "32", 1999),
                new Product("Men T-Shirt", "Puma", "White", "-", "L", 699),
                new Product("Men Hoodie", "HRX", "Grey", "-", "XL", 1499),
                new Product("Men Pant", "Arrow", "Brown", "-", "34", 1699),
                new Product("Men Kurta", "FabIndia", "Yellow", "-", "L", 1299),
                new Product("Men Jacket", "Wildcraft", "Black", "-", "L", 2499),
                new Product("Men Shorts", "Nike", "Blue", "-", "M", 999),
                new Product("Men Trackpant", "Adidas", "Grey", "-", "L", 1399),
                new Product("Men Formals", "Raymond", "Black", "-", "XL", 2599)
        };

        products[1][1] = new Product[]{
                new Product("Women Saree", "Kalaniketan", "Red", "-", "-", 2500),
                new Product("Women Dress", "Global Desi", "Blue", "-", "M", 1800),
                new Product("Women Kurti", "W", "Pink", "-", "L", 1200),
                new Product("Women Jeans", "Only", "Black", "-", "28", 1500),
                new Product("Women Top", "Zara", "White", "-", "S", 899),
                new Product("Women Gown", "Biba", "Gold", "-", "M", 3100),
                new Product("Women T-Shirt", "H&M", "Purple", "-", "M", 799),
                new Product("Women Jacket", "Puma", "Grey", "-", "L", 2200),
                new Product("Women Skirt", "Westside", "Pink", "-", "M", 1100),
                new Product("Women Palazzo", "W", "Cream", "-", "L", 999)
        };

        products[1][2] = new Product[]{
                new Product("Kids Shirt", "Babyhug", "Blue", "-", "5Y", 499),
                new Product("Kids Shorts", "Cucumber", "Green", "-", "6Y", 399),
                new Product("Kids Frock", "FirstCry", "Pink", "-", "4Y", 699),
                new Product("Kids Jeans", "LittleStars", "Black", "-", "7Y", 799),
                new Product("Kids T-Shirt", "Puma", "Yellow", "-", "5Y", 550),
                new Product("Kids Dress", "BabyGo", "Purple", "-", "3Y", 850),
                new Product("Kids Shoes", "Lilliput", "Red", "-", "8Y", 999),
                new Product("Kids Jacket", "H&M", "Grey", "-", "6Y", 1299),
                new Product("Kids Kurta", "FabIndia", "Orange", "-", "5Y", 999),
                new Product("Kids Trackpant", "Nike", "Blue", "-", "6Y", 599)
        };

        products[1][3] = new Product[]{
                new Product("Sports Shoes", "Nike", "Black", "-", "9", 2999),
                new Product("Casual Shoes", "Puma", "White", "-", "8", 2599),
                new Product("Sandals", "Bata", "Brown", "-", "7", 899),
                new Product("Flip Flops", "Adidas", "Blue", "-", "8", 499),
                new Product("Formal Shoes", "Woodland", "Black", "-", "9", 3499),
                new Product("Heels", "Catwalk", "Red", "-", "6", 1299),
                new Product("Sneakers", "HRX", "Grey", "-", "8", 1699),
                new Product("Running Shoes", "Reebok", "Black", "-", "9", 2799),
                new Product("Kids Shoes", "Crocs", "Green", "-", "4", 999),
                new Product("Ladies Flats", "Metro", "Pink", "-", "6", 699)
        };

        // Home Decor
        products[2][0] = new Product[]{
                new Product("Wall Clock", "Ajanta", "Black", "-", "-", 699),
                new Product("Photo Frame", "HomeCentre", "Brown", "-", "-", 499),
                new Product("Wall Art", "ArtVilla", "Multicolor", "-", "-", 999),
                new Product("Mirror", "DMart", "Silver", "-", "-", 1499),
                new Product("Wall Basket", "Terra", "Brown", "-", "-", 799),
                new Product("Wall Hangings", "CraftsVilla", "Orange", "-", "-", 599),
                new Product("Wall Lamp", "Philips", "Yellow", "-", "-", 1299),
                new Product("Designer Clock", "Titan", "Black", "-", "-", 1999),
                new Product("LED Frame", "Syska", "White", "-", "-", 1599),
                new Product("Wooden Panel", "UrbanLadder", "Brown", "-", "-", 2500)
        };

        products[2][1] = new Product[]{
                new Product("Table Lamp", "Philips", "White", "-", "-", 899),
                new Product("LED Strip", "Syska", "RGB", "-", "-", 699),
                new Product("Ceiling Light", "Havells", "Yellow", "-", "-", 1499),
                new Product("Night Lamp", "Wipro", "Blue", "-", "-", 499),
                new Product("Floor Lamp", "HomeTown", "Black", "-", "-", 1800),
                new Product("Hanging Light", "CraftsVilla", "Brown", "-", "-", 2200),
                new Product("Spotlight", "Havells", "White", "-", "-", 999),
                new Product("Decor Bulb", "Syska", "Warm", "-", "-", 350),
                new Product("Neon Light", "Philips", "Pink", "-", "-", 599),
                new Product("Smart Bulb", "Wipro", "RGB", "-", "-", 999)
        };

        products[2][2] = new Product[]{
                new Product("Indoor Plant", "Ugaoo", "Green", "-", "-", 499),
                new Product("Money Plant", "NurseryLive", "Green", "-", "-", 299),
                new Product("Snake Plant", "Ugaoo", "Green", "-", "-", 799),
                new Product("Bamboo Plant", "Amazon", "Green", "-", "-", 399),
                new Product("Flower Pot", "ClayArt", "Red", "-", "-", 199),
                new Product("Artificial Plant", "DecoArts", "Green", "-", "-", 999),
                new Product("Big Indoor Plant", "UrbanLadder", "Green", "-", "-", 1499),
                new Product("Hanging Plant", "CraftsVilla", "Green", "-", "-", 499),
                new Product("Bonsai Plant", "Ugaoo", "Green", "-", "-", 1299),
                new Product("Garden Pot", "NurseryLive", "Brown", "-", "-", 199)
        };

        products[2][3] = new Product[]{
                new Product("Elephant Showpiece", "CraftsVilla", "Gold", "-", "-", 799),
                new Product("Flower Vase", "HomeCentre", "White", "-", "-", 699),
                new Product("Crystal Ball", "CrystalArt", "Transparent", "-", "-", 999),
                new Product("Human Statue", "ArtVilla", "Black", "-", "-", 999),
                new Product("Metal Art", "UrbanLadder", "Gold", "-", "-", 1299),
                new Product("Candle Holder", "DMart", "Silver", "-", "-", 499),
                new Product("Toy Buddha", "CraftsVilla", "Yellow", "-", "-", 899),
                new Product("Art Sculpture", "ArtVilla", "Brown", "-", "-", 1599),
                new Product("Mini Showpiece", "HomeTown", "Pink", "-", "-", 299),
                new Product("Golden Horse", "UrbanLadder", "Gold", "-", "-", 1999)
        };

        // Household
        products[3][0] = new Product[]{
                new Product("Gas Stove", "Prestige", "Black", "-", "-", 2999),
                new Product("Mixer Grinder", "Philips", "White", "-", "-", 2499),
                new Product("Pressure Cooker", "Hawkins", "Silver", "-", "-", 1599),
                new Product("Knife Set", "Pigeon", "Black", "-", "-", 799),
                new Product("Dinner Set", "Corelle", "White", "-", "-", 3999),
                new Product("Kettle", "Prestige", "Silver", "-", "-", 1499),
                new Product("Chopper", "Pigeon", "Blue", "-", "-", 499),
                new Product("Spoon Set", "Cello", "Silver", "-", "-", 299),
                new Product("Cutting Board", "Amazon", "Brown", "-", "-", 299),
                new Product("Rice Cooker", "Panasonic", "White", "-", "-", 1899)
        };

        products[3][1] = new Product[]{
                new Product("Mop", "ScotchBrite", "Green", "-", "-", 499),
                new Product("Broom", "Gala", "Blue", "-", "-", 199),
                new Product("Cleaning Gloves", "DMart", "Yellow", "-", "-", 99),
                new Product("Toilet Brush", "Gala", "White", "-", "-", 149),
                new Product("Glass Cleaner", "Colin", "Blue", "-", "-", 95),
                new Product("Phenyl", "Harpic", "White", "-", "-", 120),
                new Product("Dish Scrubber", "ScotchBrite", "Green", "-", "-", 40),
                new Product("Cleaning Spray", "Colin", "White", "-", "-", 150),
                new Product("Wipes", "Origami", "Pink", "-", "-", 120),
                new Product("Bathroom Cleaner", "Harpic", "Blue", "-", "-", 160)
        };

        products[3][2] = new Product[]{
                new Product("Laundry Bag", "Amazon", "Grey", "-", "-", 399),
                new Product("Laundry Basket", "HomeCentre", "Blue", "-", "-", 599),
                new Product("Detergent", "Surf Excel", "White", "-", "-", 199),
                new Product("Softener", "Comfort", "Pink", "-", "-", 220),
                new Product("Bleach", "Ujala", "White", "-", "-", 50),
                new Product("Cloth Clips", "Pigeon", "Multicolor", "-", "-", 79),
                new Product("Iron", "Philips", "White", "-", "-", 999),
                new Product("Mini Iron Box", "Bajaj", "Blue", "-", "-", 699),
                new Product("Laundry Powder", "Tide", "Yellow", "-", "-", 120),
                new Product("Washing Brush", "Gala", "Blue", "-", "-", 49)
        };

        products[3][3] = new Product[]{
                new Product("Plastic Box", "Cello", "Blue", "-", "-", 199),
                new Product("Storage Rack", "HomeTown", "Brown", "-", "-", 999),
                new Product("Steel Container", "Pigeon", "Silver", "-", "-", 399),
                new Product("Wooden Shelf", "UrbanLadder", "Brown", "-", "-", 1799),
                new Product("Glass Jars", "Amazon", "Transparent", "-", "-", 299),
                new Product("Water Bottle", "Milton", "Blue", "-", "-", 299),
                new Product("Tiffin Box", "Cello", "Red", "-", "-", 399),
                new Product("Big Basket", "DMart", "Black", "-", "-", 249),
                new Product("Fridge Organizer", "Amazon", "White", "-", "-", 350),
                new Product("Food Container", "Tupperware", "Pink", "-", "-", 499)
        };
    }

    public static void main(String[] args) {
        new Ecommerce().setVisible(true);
    }
}


//////////////////////////
// PRODUCT CLASS
//////////////////////////

class Product {
    String name, brand, color, ram, storage;
    double price;

    Product(String n, String b, String c, String r, String s, double p) {
        name = n;
        brand = b;
        color = c;
        ram = r;
        storage = s;
        price = p;
    }
}

//////////////////////////
// ADD BUTTON RENDERER
//////////////////////////

class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
        setBackground(new Color(33, 150, 243));
        setForeground(Color.WHITE);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean selected, boolean focused,
                                                   int row, int col) {
        setText("Add");
        return this;
    }
}

//////////////////////////
// ADD BUTTON EDITOR
//////////////////////////

class ButtonEditor extends DefaultCellEditor {

    private JButton btn;
    private boolean clicked;
    private Ecommerce main;
    private int row;

    public ButtonEditor(JCheckBox cb, Ecommerce m) {
        super(cb);
        this.main = m;

        btn = new JButton("Add");
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.WHITE);

        btn.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean selected, int r, int col) {
        row = r;
        clicked = true;
        return btn;
    }

    public Object getCellEditorValue() {
        if (clicked) main.addFromTable(row);
        clicked = false;
        return "Add";
    }
}