
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

public class MainFrame {

    private JFrame frame;
    private JPanel gridPanel;
    public JPanel productListPanel;
    static JPanel cards;
    static CardLayout cardLayout;

    SingleProduct singleProduct;


    public MainFrame() {
        initialize();
    }

    public void initialize() {

        cards = new JPanel();
        cardLayout = new CardLayout();
        cards.setLayout(cardLayout);

        frame = new JFrame();
        frame.setTitle("Woodora Furniture");
//        frame.setLayout(new BorderLayout(10, 5));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        JPanel HeaderMain = new JPanel(new BorderLayout());
        HeaderMain.setBackground(new Color(0x9cdccc));

        JPanel headerBody = new JPanel(new BorderLayout());
        headerBody.setBackground(new Color(0x64A48C));
        headerBody.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel navlinks = new JPanel(new FlowLayout());
        navlinks.setBorder(BorderFactory.createEmptyBorder(15, 650, 0, 0));
        navlinks.setBackground(new Color(0x64A48C));

        JLabel home = new JLabel("FURNITURE");
        home.setFont(new Font("Arial", Font.BOLD, 15));
        home.setForeground(Color.WHITE);

        JLabel savedtemps = new JLabel("MY TEMPLATES");
        savedtemps.setFont(new Font("Arial", Font.BOLD, 15));
        savedtemps.setForeground(Color.WHITE);

        savedtemps.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cards.add(new SavedTemplates().initialize(), "template");
                cardLayout.show(cards, "template");
            }
        });

        navlinks.add(home);
        navlinks.add(Box.createHorizontalStrut(20));
        navlinks.add(savedtemps);

        ImageIcon WoodoraLogoIcon = new ImageIcon(getClass().getResource("Woodora-logo.png"));
        Image WoodorascaledImage = WoodoraLogoIcon.getImage().getScaledInstance(140, 55, Image.SCALE_SMOOTH);
        ImageIcon scaledWoodoraLogoIcon = new ImageIcon(WoodorascaledImage);
        JLabel WoodoraLogoLabel = new JLabel(scaledWoodoraLogoIcon);

        ImageIcon UserIcon = new ImageIcon(getClass().getResource("1177568.png"));
        Image UserIconImage = UserIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon UserIconImageIcon = new ImageIcon(UserIconImage);
        JLabel UserIconLabel = new JLabel(UserIconImageIcon);
        UserIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               cardLayout.show(cards, "account");
            }
        });


        JPanel fullrowheader = new JPanel();
        fullrowheader.setBackground(new Color(0x64A48C));
        fullrowheader.setLayout(new BoxLayout(fullrowheader, BoxLayout.X_AXIS));
        fullrowheader.add(WoodoraLogoLabel);
        fullrowheader.add(navlinks);
        fullrowheader.add(UserIconLabel);

        headerBody.add(fullrowheader);
        HeaderMain.add(headerBody, BorderLayout.CENTER);

        productListPanel = new JPanel();
        productListPanel.setBackground(Color.WHITE);
//        productListPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        productListPanel.setLayout(new BorderLayout(0, 0));

        gridPanel = new JPanel(new GridLayout(0, 4, 0, 0));
        gridPanel.setBackground(Color.WHITE);

        List<Product> products = loadProductsFromJson(MainFrame.class.getResource("products.json"));

        if (products != null) {
            products.sort(Comparator.comparingDouble(p -> {
                String price = p.getProductPrice().replace(" LKR", "").replace(",", "");
                return Double.parseDouble(price);
            }));

            for (Product product : products) {
                addImagePanel(product);
            }
        } else {
            System.err.println("Failed to load...");
        }
        productListPanel.add(gridPanel);
        productListPanel.add(HeaderMain, BorderLayout.NORTH);
        cards.add(new WelcomePanel(), "welcome");
        cards.add(new LoginPanel(), "login");
        cards.add(new RegisterPanel(), "register");
        cards.add(productListPanel, "home");
        cards.add(new AccountPanel(), "account");
        cards.add(new ForgotPasswordPanel(), "forgot");
        frame.add(cards);
        frame.setVisible(true);
    }

    private List<Product> loadProductsFromJson(URL resourceURL) {
        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>() {
        }.getType();
        try (InputStreamReader reader = new InputStreamReader(resourceURL.openStream())) {
            return gson.fromJson(reader, productListType);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the exception as needed
        }
    }

    private void addImagePanel(Product product) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ImageIcon icon = new ImageIcon(getClass().getResource(product.getProductImageName()));
        Image scaledImg = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel(product.getProductName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameLabel.setForeground(Color.WHITE);

        JPanel infoRow = new JPanel(new BorderLayout());
        infoRow.setBackground(new Color(0x64A48C));
        infoRow.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        infoRow.add(nameLabel, BorderLayout.WEST);

        panel.add(infoRow, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                singleProduct = new SingleProduct(product, cardLayout, cards);
                productListPanel.add(singleProduct);
                cards.add(singleProduct, "view");
                Product.currentId = product.getId();
                cardLayout.show(cards, "view");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(0x9cdccc));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(UIManager.getColor("Panel.background"));
            }
        });

        gridPanel.add(panel);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
