
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import org.stjs.javascript.dom.Col;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DesignSetupObject {
    private JPanel panel;
    RenderParams renderParams;

    public DesignSetupObject(String twoOrthreeD, RenderParams _renderParams) {
        this.renderParams = _renderParams;
        initialize(twoOrthreeD);
    }

    public void initialize(String twoOrthreeD) {

        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(caps);

        if (twoOrthreeD.equals("2D")) {
            Render2D render2D = new Render2D(renderParams);

            canvas.addKeyListener(render2D);
            canvas.addGLEventListener(render2D);
        } else {
            Render render = new Render(renderParams);

            canvas.addKeyListener(render);
            canvas.addGLEventListener(render);
        }


        final FPSAnimator animator = new FPSAnimator(canvas, 300, true);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.PINK);
        panel.setPreferredSize(new Dimension(1200, 720));

        JPanel topView = new JPanel();
        topView.setLayout(new BorderLayout());
        topView.setBackground(Color.LIGHT_GRAY);

        JLabel heading = new JLabel("EDIT FURNITURE IN " + twoOrthreeD);
        heading.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        heading.setFont(new Font("SansSerif", Font.BOLD, 22));
        heading.setForeground(Color.white);
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        JButton nextBtn = new JButton("EDIT ROOM");
        nextBtn.setBorder(BorderFactory.createEmptyBorder(8, 55, 10, 55));
        nextBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        nextBtn.setBackground(new Color(0x206351));
        nextBtn.setForeground(Color.white);

        nextBtn.addActionListener(e -> {
            DesignSetupRoom designSetupRoom = new DesignSetupRoom(twoOrthreeD, renderParams);
            designSetupRoom.initialize(twoOrthreeD, renderParams);

            MainFrame.cards.add(designSetupRoom.getPanelDesSetROOM(), "editRoom");
            MainFrame.cardLayout.show(MainFrame.cards, "editRoom");
        });


        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(0x64a48c));
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));

        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(nextBtn);
        btnPanel.add(Box.createHorizontalStrut(10));
        btnPanel.add(Box.createHorizontalGlue());

        JPanel bothPanels = new JPanel(new BorderLayout());

        JPanel model = new JPanel(new BorderLayout());
        model.setBackground(Color.BLUE);
        model.setPreferredSize(new Dimension(700, 200));
        model.add(canvas);

        JPanel variationPanel = new JPanel();
        variationPanel.setBackground(new Color(0x64a48c));
        variationPanel.setPreferredSize(new Dimension(300, 150));

        JPanel BaseColorSet = new JPanel(new GridLayout(2, 1));
        BaseColorSet.setBackground(new Color(0x64a48c));
        BaseColorSet.setPreferredSize(new Dimension(280, 100));
        BaseColorSet.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JLabel basecolor = new JLabel("COLOR OF THE BASE: ");
        basecolor.setForeground(Color.WHITE);
        basecolor.setFont(new Font("SansSerif", Font.BOLD, 14));
        String[] basecolors = {"DEFAULT", "RED", "BLUE", "GREEN", "ORANGE"};
        JComboBox<String> baseColrOps = new JComboBox<>(basecolors);
        baseColrOps.setFont(new Font("SansSerif", Font.BOLD, 14));

        baseColrOps.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String topColor = (String) baseColrOps.getSelectedItem();

                if (topColor.equals("RED")) {
                    renderParams.setTopColor(new float[]{1, 0, 0});
                    Render2D.topColor = new float[]{1, 0, 0};
                    Render.topColor = new float[]{1, 0, 0};
                } else if (topColor.equals("BLUE")) {
                    renderParams.setTopColor(new float[]{0, 0, 1});
                    Render2D.topColor = new float[]{0, 0, 1};
                    Render.topColor = new float[]{0, 0, 1};
                } else if (topColor.equals("GREEN")) {
                    renderParams.setTopColor(new float[]{0, 1, 0});
                    Render2D.topColor = new float[]{0, 1, 0};
                    Render.topColor = new float[]{0, 1, 0};
                }
            }
        });

        BaseColorSet.add(basecolor);
        BaseColorSet.add(baseColrOps);
        JPanel LegColorSet = new JPanel(new GridLayout(2, 1));
        LegColorSet.setBackground(new Color(0x64a48c));
        LegColorSet.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JLabel legcolor = new JLabel("LEG COLOR");
        legcolor.setForeground(Color.WHITE);
        legcolor.setFont(new Font("SansSerif", Font.BOLD, 14));
        String[] legcolors = {"DEFAULT", "RED", "BLUE", "GREEN", "ORANGE"};
        JComboBox<String> legColrOps = new JComboBox<>(legcolors);
        legColrOps.setFont(new Font("SansSerif", Font.BOLD, 14));
        legColrOps.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String legColor = (String) legColrOps.getSelectedItem();

                if (legColor.equals("RED")) {
                    renderParams.setLegColor(new float[]{1, 0, 0});
                    Render2D.legColor = new float[]{1, 0, 0};
                    Render.legColor = new float[]{1, 0, 0};
                } else if (legColor.equals("BLUE")) {
                    renderParams.setLegColor(new float[]{0, 0, 1});
                    Render.legColor = new float[]{0, 0, 1};
                } else if (legColor.equals("GREEN")) {
                    renderParams.setLegColor(new float[]{0, 1, 0});
                    Render.legColor = new float[]{0, 1, 0};
                }
            }
        });

        LegColorSet.add(legcolor);
        LegColorSet.add(legColrOps);

        JPanel WholeColorSet = new JPanel(new GridLayout(2, 1));
        WholeColorSet.setBackground(new Color(0x64a48c));
        WholeColorSet.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JLabel wholecolor = new JLabel("FULL COLOR");
        wholecolor.setForeground(Color.WHITE);
        wholecolor.setFont(new Font("SansSerif", Font.BOLD, 14));
        String[] wholecolors = {"DEFAULT", "RED", "BLUE", "GREEN", "ORANGE"};
        JComboBox<String> WholeColrOps = new JComboBox<>(wholecolors);
        WholeColrOps.setFont(new Font("SansSerif", Font.BOLD, 14));

        WholeColorSet.add(wholecolor);
        WholeColorSet.add(WholeColrOps);

        JPanel gridPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        gridPanel.setBackground(new Color(0x64a48c));
        gridPanel.add(BaseColorSet);
        gridPanel.add(LegColorSet);
        gridPanel.add(WholeColorSet);

        variationPanel.add(gridPanel, BorderLayout.CENTER);

        bothPanels.add(model, BorderLayout.CENTER);
        bothPanels.add(variationPanel, BorderLayout.SOUTH);


        JPanel navPan = new JPanel(new BorderLayout());
        navPan.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        navPan.setBackground(new Color(0x64a48c));
        navPan.add(heading, BorderLayout.WEST);
        navPan.add(btnPanel, BorderLayout.EAST);

        topView.add(navPan, BorderLayout.NORTH);
        topView.add(bothPanels);

        panel.add(topView);
        animator.start();
    }

    public JPanel getPanelDesSetOBJ() {
        return panel;
    }
}
