import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class AWTGoogleCrawler extends JFrame{
	private JLabel keywordsLabel;
	private JTextField keywords;
	private JButton searchBtn;
	private JPanel controls;
	private JPanel Results;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel southPanel;
	private String GoogleSearch="http://www.google.com/search?q=";
//	private String charset="UTF-8";
	private String userAgent="Moonlove";
	private String url="";
	private JTextArea searchResults;
	public AWTGoogleCrawler()
	{
		initUI();
	}
	private void initUI() { 
        controls = new JPanel(new GridBagLayout());
        Results=new JPanel(new GridBagLayout());
        leftPanel = new JPanel(new GridBagLayout());
        rightPanel=new JPanel(new GridBagLayout());
        southPanel = new JPanel(new GridBagLayout());
        
        Results.setBorder(new TitledBorder(new EtchedBorder(),"Search Results"));
        searchBtn=new JButton("Search");
        keywordsLabel=new JLabel("Keyword: ");
        keywords=new JTextField("Enter keywords here.");
               
        searchResults=new JTextArea(32,58);
        searchResults.setBorder(BorderFactory.createDashedBorder(Color.black));
        searchResults.setVisible(false);
        searchResults.setEditable(false);
        JScrollPane scrollbar=new JScrollPane(searchResults);
        scrollbar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        keywords.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
//				//System.out.println(urlText.getText());
//				url+=urlText.getText();
//				url+="\n";
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
//				//System.out.println(urlText.getText());
//				url+=urlText.getText();
//				url+="\n";
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				
			}
		});
        searchBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String charset="UTF-8";
				if(!searchResults.isVisible())
					searchResults.setVisible(true);
				for(int i=0;i<100;i+=10)
				{
					try {
						Elements links=Jsoup.connect(GoogleSearch+URLEncoder.encode(keywords.getText(), charset)+"&start="+i).userAgent(userAgent).get().select("li.g>h3>a");
						for(Element link:links)
						{
							String title=link.text();
							String linkURL=link.absUrl("href");
						
							linkURL=URLDecoder.decode(linkURL.substring(linkURL.indexOf('=')+1, linkURL.indexOf('&')),"UTF-8");
							if(!linkURL.startsWith("http"))
								continue;
							searchResults.setText(searchResults.getText()+"Title: "+title+"\n");
							searchResults.setText(searchResults.getText()+"URL: : "+linkURL+"\n");
						
						}
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				pack();
			}
		});
        GridBagConstraints cNorth = new GridBagConstraints();
        cNorth.weightx=1.0;
        cNorth.fill=GridBagConstraints.HORIZONTAL;
        
        GridBagConstraints cSouth = new GridBagConstraints();
        cSouth.weightx=1.0;
        cSouth.fill=GridBagConstraints.VERTICAL;
        
        Results.add(scrollbar);
        
        controls.add(new JPanel());
        controls.add(keywordsLabel);
        controls.add(keywords,cNorth);
        controls.add(searchBtn);          
        controls.add(new JPanel());
        
        leftPanel.add(new JPanel());
        rightPanel.add(new JPanel());
        southPanel.add(new JPanel());
        
        setLayout(new BorderLayout());
        add(controls, BorderLayout.NORTH);
        add(Results,BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel,BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);
        
        pack();
    }
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new AWTGoogleCrawler().setVisible(true);				
			}
		});

	}	

}