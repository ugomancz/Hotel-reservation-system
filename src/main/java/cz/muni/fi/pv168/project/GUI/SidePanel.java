package cz.muni.fi.pv168.project.GUI;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {
    public SidePanel() {
        super();
        this.setBackground(MainPanel.mainBackground);
        this.setLayout(new BorderLayout(0, 10));
        this.setPreferredSize(new Dimension(Button.dimension.width, 500));

        this.add(new ButtonPanel(), BorderLayout.CENTER);
        this.add(new CalendarPanel(), BorderLayout.SOUTH);
    }
}
