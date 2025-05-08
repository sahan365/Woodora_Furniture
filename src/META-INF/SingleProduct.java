import javax.swing.*;
import java.awt.*;

public class SingleProduct extends JPanel {
    CardLayout cardLayout;
    JPanel cards;

    public SingleProduct(Product product, CardLayout cardLayout, JPanel cards) {
        initialize(product);
        this.cardLayout = cardLayout;
        this.cards = cards;
    }

    public void initialize(Product product) {
        setLayout(new BorderLayout(10, 5));

        JPanel singleProductPanel = new JPanel(new GridBagLayout());
        singleProductPanel.setBackground(new Color(0x64A48C));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        JLabel productNameLabel = new JLabel("<html>" + product.getProductName() + "</html>");
        productNameLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        productNameLabel.setForeground(Color.BLUE);
        productNameLabel.setBorder(BorderFactory.createEmptyBorder(45, 0, 12, 0));
//        productNameLabel.setPreferredSize(new Dimension(300, productNameLabel.getPreferredSize().height + 100));
        productNameLabel.setVerticalAlignment(JLabel.TOP);
        productNameLabel.setHorizontalAlignment(JLabel.LEFT);

        ImageIcon productImageIcon = new ImageIcon(getClass().getResource(product.getProductImageName()));
        Image scaledImage = productImageIcon.getImage().getScaledInstance(580, 620, Image.SCALE_SMOOTH);
        ImageIcon scaledProductImageIcon = new ImageIcon(scaledImage);
        JLabel productImageLabel = new JLabel(scaledProductImageIcon);

        JLabel productCategoryLabel = new JLabel("Category: " + product.getProductCategory());
        productCategoryLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        productCategoryLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel productColorLabel = new JLabel("Color: " + product.getProductColor());
        productColorLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        productColorLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel productMaterialLabel = new JLabel("Material: " + product.getProductMaterial());
        productMaterialLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        productMaterialLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel productDescriptionLabel = new JLabel("<html>Description: " + product.getProductDescription() + "</html>");
        productDescriptionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        productDescriptionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        productDescriptionLabel.setPreferredSize(new Dimension(300, productDescriptionLabel.getPreferredSize().height + 150));
        productDescriptionLabel.setVerticalAlignment(JLabel.TOP);
        productDescriptionLabel.setHorizontalAlignment(JLabel.LEFT);

        JLabel productPriceLabel = new JLabel("Price: " + product.getProductPrice());
        productPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        productPriceLabel.setForeground(Color.BLUE);
        productPriceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
//        leftCol.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 0));
        leftCol.setBackground(new Color(0x64A48C));

        JPanel mainImage = new JPanel(new GridBagLayout());
        mainImage.setBackground(new Color(0x64A48C));
        mainImage.add(productImageLabel);
        leftCol.add(Box.createVerticalGlue());
        leftCol.add(mainImage);
        leftCol.add(Box.createVerticalGlue());

        JPanel rightCol = new JPanel(new BorderLayout());
        rightCol.setBorder(BorderFactory.createEmptyBorder(15, 25, 0, 25));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));

        JPanel fourGrid = new JPanel(new GridLayout(1, 4));
//        fourGrid.setBackground(Color.WHITE);
//        fourGrid.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        for (int i = 0; i < 4; i++) {
            Image thumbScaled = productImageIcon.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
            ImageIcon scaledThumbIcon = new ImageIcon(thumbScaled);
            JLabel thumbImgLabel = new JLabel(scaledThumbIcon);
            fourGrid.add(thumbImgLabel);
        }

        JButton view3Dbtn = new JButton("3D MODEL");
        view3Dbtn.setBorder(BorderFactory.createEmptyBorder(13, 40, 15, 40));
        view3Dbtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        view3Dbtn.setBackground(new Color(0x206351));
        view3Dbtn.setForeground(Color.WHITE);


        JButton view2Dbtn = new JButton("2D MODEL");
        view2Dbtn.setBorder(BorderFactory.createEmptyBorder(13, 40, 15, 40));
        view2Dbtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        view2Dbtn.setBackground(new Color(0x206351));
        view2Dbtn.setForeground(Color.WHITE);


        RenderParams renderParams = new RenderParams();
        view2Dbtn.addActionListener(e -> {
            renderParams.setId(Product.currentId);
            ViewIn2D viewIn2D = new ViewIn2D(renderParams);
            viewIn2D.initialize();
            cards.add(viewIn2D.getPanel2D(), "2d");
            cardLayout.show(cards, "2d");
        });

        view3Dbtn.addActionListener(e -> {
            ViewIn3D viewIn3D = new ViewIn3D(renderParams);
            viewIn3D.initialize();
            cards.add(viewIn3D.getPanel3D(), "3d");
            cardLayout.show(cards, "3d");
        });

        JButton backBtn = new JButton("Back");
        backBtn.setBorder(BorderFactory.createEmptyBorder(13, 25, 15, 25));
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        backBtn.setBackground(Color.darkGray);
        backBtn.setForeground(Color.WHITE);

        backBtn.addActionListener(e -> cardLayout.show(cards, "home"));

        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(view2Dbtn);
        buttonRow.add(Box.createHorizontalStrut(10));
        buttonRow.add(view3Dbtn);
        buttonRow.add(Box.createHorizontalStrut(10));
        buttonRow.add(backBtn);
        buttonRow.add(Box.createHorizontalGlue());

        btnPanel.add(fourGrid);
//        btnPanel.add(Box.createVerticalStrut(5));
        btnPanel.add(buttonRow);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        infoPanel.add(productNameLabel);
        infoPanel.add(productCategoryLabel);
        infoPanel.add(productColorLabel);
        infoPanel.add(productMaterialLabel);
        infoPanel.add(productDescriptionLabel);
        infoPanel.add(productPriceLabel);

        rightCol.add(infoPanel, BorderLayout.NORTH);
        rightCol.add(btnPanel, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.weightx = 0.9;
        singleProductPanel.add(leftCol, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.1;
        singleProductPanel.add(rightCol, gbc);

        add(singleProductPanel);
    }
}