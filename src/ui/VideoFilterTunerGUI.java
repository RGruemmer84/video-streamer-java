package ui;

import image.FilterTypes;
import image.Filters;
import image.ImageFilter;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import image.ImageFilterParameter;
import static image.ImageFilterParameter.PrimitiveType.DOUBLE;
import image.filter.PassThruFilter;
import video.VideoStreamer;
import video.VideoConfiguration;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;


public final class VideoFilterTunerGUI extends JFrame{
    private static final String TITLE_BAR_STRING = "Stream";
    private static final int GAP = 10;
    
    private VideoStreamer streamer;
    private final FilterChainPanel filterChainPanel;
    private final FilterTuningPanel filterTunerPanel;
    private final PlayerPanel playerPanel;
    
    public VideoFilterTunerGUI(VideoStreamer stream){
        super(TITLE_BAR_STRING);
        streamer = stream;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(VideoFilterTunerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JPanel framePanel = new JPanel();
        framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.Y_AXIS));
        
        framePanel.add(new JLabel(streamer.getParams().stream_name));
        
        JPanel corePanel = new JPanel();
        corePanel.setLayout(new BoxLayout(corePanel, BoxLayout.X_AXIS));
        filterChainPanel = new FilterChainPanel(streamer.getFilterSetBuffer());
        filterTunerPanel = new FilterTuningPanel(
                streamer.getFilterSetBuffer().isEmpty()
                ? new PassThruFilter() : streamer.getFilterSetBuffer().get(0));
        playerPanel = new PlayerPanel(streamer);
        playerPanel.startPlayer();
        corePanel.add(filterChainPanel);
        corePanel.add(Box.createRigidArea(new Dimension(GAP,0)));
        corePanel.add(filterTunerPanel);
        corePanel.add(Box.createRigidArea(new Dimension(GAP,0)));
        corePanel.add(playerPanel);
        corePanel.add(Box.createRigidArea(new Dimension(GAP,0)));
        framePanel.add(corePanel);
        add(framePanel);
        pack();
        setVisible(true);
    }
    
    
    
    //Start of inner classes for FilterChainPanel
    final class FilterChainPanel extends JPanel{
        private static final int LIST_WIDTH = 100;
        private static final int LIST_HEIGHT = 100;
        private static final int DROP_DOWN_WIDTH = 100;
        private static final int DROP_DOWN_HEIGHT = 20;
        private static final int FILTER_BUTTON_WIDTH = 100;
        private static final int FILTER_BUTTON_HEIGHT = 20;
        private static final int MASTER_BUTTON_WIDTH = 80;
        private static final int MASTER_BUTTON_HEIGHT = 40;
        
        FilterChainListPanel listPanel;
        FilterChainButtonPanel buttonPanel;
        MasterButtonPanel masterButtonPanel;

        private List<ImageFilter> workingFilterSet;

        public FilterChainPanel(List<ImageFilter> filterSetBuffer) {
            workingFilterSet = filterSetBuffer;
            drawPanel();
        }
        
        public void setFilterSetBuffer(List<ImageFilter> filterSetBuffer){
            this.workingFilterSet = filterSetBuffer;
            drawPanel();
        }
        
        public void drawPanel(){
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEtchedBorder());
            JPanel corePanel = new JPanel();
            corePanel.setLayout(new BoxLayout(corePanel, BoxLayout.X_AXIS));
            listPanel = new FilterChainListPanel();
            buttonPanel = new FilterChainButtonPanel();
            corePanel.add(listPanel);
            corePanel.add(buttonPanel);
            masterButtonPanel = new MasterButtonPanel();
            add(corePanel);
            add(Box.createRigidArea(new Dimension(0,40)));
            add(masterButtonPanel);
        }
    
        final class FilterChainListPanel extends JPanel{
            JComboBox filterComboBox;
            JList filterChainList;

            FilterChainListPanel(){
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                filterComboBox = new JComboBox();
                filterComboBox.setModel(new DefaultComboBoxModel(FilterTypes.values()));
                filterComboBox.setPreferredSize(new Dimension(DROP_DOWN_WIDTH, DROP_DOWN_HEIGHT));

                filterChainList = new JList();
                filterChainList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                filterChainList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                filterChainList.setPreferredSize(new Dimension(LIST_WIDTH, LIST_HEIGHT));
                
                filterChainList.addListSelectionListener((ListSelectionEvent lse) -> {
                    int index = filterChainList.getSelectedIndex();
                    if (index >= 0){    //NOT SURE WHY THIS CONDITIONAL IS NEEDED
                        ImageFilter f = workingFilterSet.get(index);
                        filterTunerPanel.setFilter(f);
                    }
                });
                updateList();

                add(filterComboBox);
                add(Box.createRigidArea(new Dimension(0,15)));
                add(filterChainList);
            }

            public void updateList(){
                DefaultListModel listModel = new DefaultListModel();
                for (ImageFilter f : workingFilterSet){
                    listModel.addElement(f.getFilterType().toString());
                }
                filterChainList.setModel(listModel);
            }
        }

        final class FilterChainButtonPanel extends JPanel{
            JButton addButton;
            JButton upButton;
            JButton downButton;
            JButton enableButton;
            JButton removeButton;

            FilterChainButtonPanel(){
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

                addButton = new JButton("Add");
                upButton = new JButton("Move Up");
                downButton = new JButton("Move Down");
                enableButton = new JButton("Enable");
                removeButton = new JButton("Remove");

                addButton.setPreferredSize(new Dimension(FILTER_BUTTON_WIDTH, FILTER_BUTTON_HEIGHT));
                upButton.setPreferredSize(new Dimension(FILTER_BUTTON_WIDTH, FILTER_BUTTON_HEIGHT));
                downButton.setPreferredSize(new Dimension(FILTER_BUTTON_WIDTH, FILTER_BUTTON_HEIGHT));
                enableButton.setPreferredSize(new Dimension(FILTER_BUTTON_WIDTH, FILTER_BUTTON_HEIGHT));
                removeButton.setPreferredSize(new Dimension(FILTER_BUTTON_WIDTH, FILTER_BUTTON_HEIGHT));

                addButton.setSize(new Dimension(FILTER_BUTTON_WIDTH, FILTER_BUTTON_HEIGHT));

                add(addButton);
                add(upButton);
                add(downButton);
                add(enableButton);
                add(removeButton);

                addButton.addActionListener((ActionEvent ae) -> {
                    if (listPanel.filterComboBox.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(FilterChainButtonPanel.this.getParent().getParent(), "Please select a filter");
                    } else {
                        ImageFilter f;
                        try {
                            f = Filters.createFilter((FilterTypes)listPanel.filterComboBox.getSelectedItem());
                            workingFilterSet.add(f);
                        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                            Logger.getLogger(VideoFilterTunerGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        listPanel.updateList();
                    }
                });

                upButton.addActionListener((ActionEvent ae) -> {
                    int index = listPanel.filterChainList.getSelectedIndex();
                    if (index < 0 )
                        JOptionPane.showMessageDialog(this.getParent().getParent(), "Choose a filter to move");
                    else if ( index == 0)
                        JOptionPane.showMessageDialog(this.getParent().getParent(), "Filter is at the top of the list");
                    else {
                        ImageFilter f = workingFilterSet.remove(index);
                        workingFilterSet.add(index - 1, f);
                        listPanel.updateList();
                    }
                });

                downButton.addActionListener((ActionEvent ae) -> {
                    int index = listPanel.filterChainList.getSelectedIndex();
                    if (index < 0 )
                        JOptionPane.showMessageDialog(this.getParent().getParent(), "Choose a filter to move");
                    else if ( index == workingFilterSet.size() - 1)
                        JOptionPane.showMessageDialog(this.getParent().getParent(), "Filter is at the bottom of the list");
                    else {
                        ImageFilter f = workingFilterSet.remove(index);
                        workingFilterSet.add(index + 1, f);
                        listPanel.updateList();
                    }
                });

                enableButton.addActionListener((ActionEvent ae) -> {
                    int index = listPanel.filterChainList.getSelectedIndex();
                    if (index < 0 )
                        JOptionPane.showMessageDialog(this.getParent().getParent(), "Choose a filter to enable/disable");
                    else {
                        boolean isEnabled = workingFilterSet.get(index).isEnabled();
                        workingFilterSet.get(index).toggleEnable();
                    }
                });

                removeButton.addActionListener((ActionEvent ae) -> {
                    int index = listPanel.filterChainList.getSelectedIndex();
                    if (index < 0 )
                        JOptionPane.showMessageDialog(this.getParent().getParent(), "Choose a filter to remove");
                    else {
                        boolean isEnabled = workingFilterSet.get(index).isEnabled();
                        workingFilterSet.remove(index);
                        listPanel.updateList();
                    }
                });

            }
        }
        
        final class MasterButtonPanel extends JPanel{
            JButton saveButton;
            JButton cancelButton;

            MasterButtonPanel(){
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

                saveButton = new JButton("Save");
                saveButton.setPreferredSize(new Dimension(MASTER_BUTTON_WIDTH, MASTER_BUTTON_HEIGHT));
                cancelButton = new JButton("Cancel");
                cancelButton.setPreferredSize(new Dimension(MASTER_BUTTON_WIDTH, MASTER_BUTTON_HEIGHT));

                add(cancelButton);
                add(Box.createRigidArea(new Dimension(50, 0)));
                add(saveButton);

                saveButton.addActionListener((ActionEvent ae) -> {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                });

                cancelButton.addActionListener((ActionEvent ae) -> {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                });
            }
        }

    }
    //End of inner classes for FilterChainPanel
    
    
    
    
    
    
    //Start of inner classes for FilterTuningPanel
    final class FilterTuningPanel extends JPanel{
        private ImageFilter filter;
        private List<ImageFilterParameter> params;

        private JLabel paramLabel;
        private BoxLayout topToBottomLayout;
        private static final int VERTICAL_GAP = 15;

        public FilterTuningPanel(ImageFilter f){
            filter = f;
            params = f.getParams();
            initPanelContents();
        }
        
        void setFilter(ImageFilter f){
            filter = f;
            params = f.getParams();
            initPanelContents();
        }
        
        public void initPanelContents(){
            this.removeAll();
            this.revalidate();
            this.repaint();
            setBorder(BorderFactory.createEtchedBorder());
            paramLabel = new JLabel();
            paramLabel.setText(filter.getFilterType().toString());

            topToBottomLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(topToBottomLayout);
            add(paramLabel);
            add(Box.createRigidArea(new Dimension(0,VERTICAL_GAP)));
            for (ImageFilterParameter p : params){
                ParamTuningPanel paramTuningPanel = new ParamTuningPanel(p);
                add(paramTuningPanel);
                add(Box.createRigidArea(new Dimension(0,VERTICAL_GAP)));
            }
        }

        final class ParamTuningPanel extends JPanel{
            private static final int ROW_HEIGHT = 20;
            private static final int PARAM_LABEL_WIDTH = 75;
            private static final int SLIDER_WIDTH = 100;
            private static final int VALUE_LABEL_WIDTH = 40;

            private final ImageFilterParameter param;
            private final BoxLayout thisRowLayout;
            private final JLabel paramLabel;
            private final JScrollBar paramSlider;
            private final JLabel curParamValueLabel;
            private static final int GAP = 20;
            private static final int SCROLL_EXTENT = 0;

            public ParamTuningPanel(ImageFilterParameter p){
                param = p;
                thisRowLayout = new BoxLayout(this, BoxLayout.X_AXIS);
                this.setLayout(thisRowLayout);

                paramLabel = new JLabel(param.getName());
                paramLabel.setPreferredSize(new Dimension(PARAM_LABEL_WIDTH, ROW_HEIGHT));

                curParamValueLabel = new JLabel(param.getCurVal().toString());
                curParamValueLabel.setPreferredSize(new Dimension(VALUE_LABEL_WIDTH, ROW_HEIGHT));

                if (param.getType() == DOUBLE){
                        int max = (int) ( (Double)param.getStartVal() + (1/(Double)param.getStepSize()) * ((Double)param.getEndVal() - (Double)param.getStartVal()) );
                        int cur = (int) ( ((Double)param.getCurVal() * (Double)param.getStepSize()) + (Double)param.getStartVal());
                        paramSlider = new JScrollBar(JScrollBar.HORIZONTAL,cur, SCROLL_EXTENT, 0, max);
                } else {
                    paramSlider = new JScrollBar(JScrollBar.HORIZONTAL, 
                        (Integer)param.getCurVal(), 
                        SCROLL_EXTENT, 
                        (Integer)param.getStartVal(), 
                        (Integer)param.getEndVal());
                }
                paramSlider.setPreferredSize(new Dimension(SLIDER_WIDTH, ROW_HEIGHT));
                paramSlider.setBorder(BorderFactory.createEtchedBorder());

                add(paramLabel);
                add(paramSlider);
                add(Box.createRigidArea(new Dimension(GAP,0)));
                add(curParamValueLabel);

                paramSlider.addAdjustmentListener((AdjustmentEvent ae) -> {
                    if (param.getType() == DOUBLE){
                        int valAsInt = ae.getValue();
                        double valAsDouble = (double) valAsInt * ((Double) param.getStepSize());
                        curParamValueLabel.setText(String.valueOf(valAsDouble));
                        param.setCurVal(valAsDouble);
                    } else {
                        int val = ae.getValue();
                        if ((int)param.getStepSize()!= 2){
                            curParamValueLabel.setText(String.valueOf(ae.getValue()));
                            param.setCurVal(ae.getValue());
                        } else if (val % 2 == 1){
                            curParamValueLabel.setText(String.valueOf(ae.getValue()));
                            param.setCurVal(ae.getValue());
                        }
                        
                    }
                });
            }
        }
        
    }
    //End of inner classes for FilterTuningPanel
    
    
    
    
    
    //Start of inner classes for PlayerPanel
    final class PlayerPanel extends JPanel{
        private final VideoStreamer streamer;
        private final PlayerScreen playerScreen;
        private final Thread playerScreenThread;

        public PlayerPanel(VideoStreamer streamer) {
            this.streamer = streamer;
            
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEtchedBorder());
            add(new VideoFormatPanel(streamer.getParams()));
            playerScreen = new PlayerScreen();
            playerScreenThread = new Thread(playerScreen);
            add(playerScreen);
        }
        
        public void startPlayer() {playerScreenThread.start();}

        final class PlayerScreen extends JPanel implements Runnable{
            private final JLabel screen;

            PlayerScreen(){
                super();
                screen = new JLabel();
                screen.setPreferredSize(new Dimension(
                        streamer.getParams().video_width, 
                        streamer.getParams().video_height));
                add(screen);
            }

            @Override
            public void run() {
                try {
                    Frame filterImage;
                    Java2DFrameConverter converter = new Java2DFrameConverter();
                    while (true){
                        if (!streamer.getFrameBuffer().isEmpty()){
                            filterImage = streamer.getFrameBuffer().poll();
                            
                            screen.setIcon(new ImageIcon(
                                    converter.getBufferedImage(filterImage)));
                        }
                        Thread.sleep((long)1000/30);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(PlayerScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }
    //End of inner classes for PlayerPanel
    
    //Start of inner classes for VideoFormatPanel
    final class VideoFormatPanel extends JPanel{
        private static final int GAP = 5;
        private static final String GRAY_SCALE = "Gray Scale";
        private static final String INVERTED = "Invert";
        private static final String VID_WIDTH = "Video Width";
        private static final String VID_HEIGHT = "Video Height";
        private static final String VID_LENGTH = "Video Length (sec)";
        private static final String VID_FPS = "Video FPS";
        private static final int ROW_HEIGHT = 20;
        private static final int LABEL_FIELD_WIDTH = 150;
        private static final int TEXT_FIELD_WIDTH = 75;


        private final VideoConfiguration params;

        public VideoFormatPanel(VideoConfiguration p){
            params = p;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            initComponents();
        }

        private void initComponents(){
            this.add(Box.createRigidArea(new Dimension(0,GAP)));
            add(new JLabel("Video Format Parameters"));
            
            this.add(Box.createRigidArea(new Dimension(0,GAP)));
            add(new LabelAndFieldLine(VID_FPS, params.video_fps));
            
            this.add(Box.createRigidArea(new Dimension(0,GAP)));
            add(new LabelAndFieldLine(VID_LENGTH, params.video_length_sec));
            
            this.add(Box.createRigidArea(new Dimension(0, GAP)));
            add(new LabelAndFieldLine(VID_WIDTH, params.video_width));
            
            this.add(Box.createRigidArea(new Dimension(0, GAP)));
            add(new LabelAndFieldLine(VID_HEIGHT, params.video_height));
            
            this.add(Box.createRigidArea(new Dimension(0, GAP)));
            
            JCheckBox grayScaleField = new JCheckBox(GRAY_SCALE, params.video_gray_scale);
            grayScaleField.addActionListener((ActionEvent ae) -> {
                params.video_gray_scale = grayScaleField.isSelected();
            });
            JCheckBox invertField = new JCheckBox(INVERTED, params.video_inverted);
            invertField.addActionListener((ActionEvent ae) -> {
                params.video_inverted = invertField.isSelected();
            });

            JPanel checkBoxes = new JPanel();
            checkBoxes.setLayout(new BoxLayout(checkBoxes, BoxLayout.X_AXIS));

            checkBoxes.add(grayScaleField);
            this.add(Box.createRigidArea(new Dimension(25, 0)));
            checkBoxes.add(invertField);
            add(checkBoxes);
        }

        final class LabelAndFieldLine extends JPanel{
            JLabel fieldLabel;
            JTextField fieldText;
            Integer param;
            String name;

            public LabelAndFieldLine(String name, Integer p){
                this.name = name;
                param = p;
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

                fieldLabel = new JLabel(name);
                fieldLabel.setPreferredSize(new Dimension(LABEL_FIELD_WIDTH, ROW_HEIGHT));

                fieldText = new JTextField(String.valueOf(param));
                fieldText.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, ROW_HEIGHT));

                add(fieldLabel);
                this.add(Box.createRigidArea(new Dimension(GAP,0)));
                add(fieldText);

                fieldText.addActionListener((ActionEvent ae) -> {
                    switch (name){
                        case VID_FPS:
                            params.video_fps = Integer.valueOf(fieldText.getText());
                            break;
                        case VID_LENGTH:
                            params.video_length_sec = Integer.valueOf(fieldText.getText());
                            break;
                        case VID_WIDTH:
                            params.video_width = Integer.valueOf(fieldText.getText());
                            break;
                        case VID_HEIGHT:
                            params.video_height = Integer.valueOf(fieldText.getText());
                            break;
                        default:
                            break;
                    }
                });
            }
        }
    }
    //End of inner classes for VideoFormatPanel
}
