import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class BooksFinder extends JFrame{
	private JLabel keywordsLabel;
	private JTextField keywords;
	private JButton searchBooks;
	private JButton searchEbooks;
	private JPanel controls;
	private JPanel Results;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel southPanel;
	
	private String charset="UTF-8";
	private String userAgent="Moonlove";
	private String url="";
	private JTextArea searchResults;
	public BooksFinder()
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
        searchBooks=new JButton("Purchase Book");
        searchEbooks=new JButton("Search EBook");
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
        searchBooks.addActionListener(new ActionListener() {

        	@Override
			public void actionPerformed(ActionEvent e) {
				if(!searchResults.isVisible())
					searchResults.setVisible(true);
				//Eslite
				try {
						
						Document Html=Jsoup.connect(SearchURL.EsliteSearch+URLEncoder.encode(keywords.getText(), charset)).userAgent("Mozilla/5.0").timeout(30000).get();
						Elements links=Html.select("td.name>h3>a>span");
						Elements productPrice=Html.select("td.summary>span.price_sale");
						searchResults.setText("誠品網路書店: \n");
						for(int i =0;i<links.size();i++)
						{
							String title=links.get(i).html();
							String price=productPrice.get(i).html();
							if(price.contains("折"))
							{
								productPrice.remove(i);
								price=productPrice.get(i).html();
							}
							if(title.contains(keywords.getText()))
							{
								title=title.replace("<em>", "");
								title=title.replace("</em>", "");
								searchResults.setText(searchResults.getText()+"\tTitle: "+title+"\n");
								searchResults.setText(searchResults.getText()+"\tPrice: : "+price+"\n");
							}		
						}
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				//KingStone
				try {
					String urlEncodedKeyword=URLEncoder.encode(keywords.getText(), charset);
					urlEncodedKeyword=urlEncodedKeyword.replace("%", "%25");
					searchResults.setText(searchResults.getText()+"\n金石堂網路書店: \n");
					Document Html=Jsoup.connect(SearchURL.KingStoneSearch+urlEncodedKeyword).userAgent("Mozilla/5.0").timeout(30000).get();
					Elements links=Html.select("li>a.anchor[title*="+keywords.getText()+"]>span");
					Elements productPrice=Html.select("li>span.price>span.sale_price");					
					
					for (int i = 0; i < links.size(); i++) {
						String title = links.get(i).html();
						
						String price = null;
//						if (title.contains(keywords.getText())) {
							if (i < productPrice.size()) {
								price = productPrice.get(i).html();
								price = price.replace("<em>", "");
								price = price.replace("</em>", "");
								searchResults.setText(searchResults.getText()
										+ "\tTitle: " + title + "\n");
								searchResults.setText(searchResults.getText()
										+ "\tPrice: " + price + "\n");
							}
//						}
					}
				}catch(SocketTimeoutException e1)
				{
					searchResults.setText(searchResults.getText()
							+ "\tConnection TimeOut !!\n");
				}
				catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//PCHomeSearch
				try {				
					searchResults.setText(searchResults.getText()+"\nPCHOME網路書店: \n");
					Document Html=Jsoup.connect(SearchURL.PCHomeSearch+URLEncoder.encode(keywords.getText(), charset)).ignoreContentType(true).userAgent("Mozilla/5.0").timeout(30000).get();
					JsonObject contentTemp = ResponceFromJSON(Html);
					String title=contentTemp.get("name").toString();
					String price=contentTemp.get("price").toString();
					title=title.replace("\"", "");
					price=price.replace("\"", "");
					searchResults.setText(searchResults.getText()
							+ "\tTitle: " + title + "\n");
					searchResults.setText(searchResults.getText()
							+ "\tPrice: " + price + "\n");
					
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//Books.com
				try {
					
					Document Html=Jsoup.connect(SearchURL.BooksSearch+URLEncoder.encode(keywords.getText(), charset)).userAgent("Mozilla/5.0").timeout(30000).get();
					Elements links=Html.select("form.result>ul.searchbook>li.item>h3>a[title*="+keywords.getText()+"]");
					Elements productPrice=Html.select("form.result>ul.searchbook>li.item>span.price>strong>b");
					searchResults.setText(searchResults.getText()+"\n博客來網路書店: \n");
					for(Element link :links)
					{
						String title=link.html();
						String price=productPrice.get(1).html();
						if(title.contains(keywords.getText()))
						{
								title=title.replace("<em>", "");
								title=title.replace("</em>", "");
								searchResults.setText(searchResults.getText()+"\tTitle: "+title+"\n");
								searchResults.setText(searchResults.getText()+"\tPrice: : "+price+"\n");
						}
					}
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//Book Store End
				pack();
			}

			private JsonObject ResponceFromJSON(Document Html) {
				String htmlBodyString=Html.body().toString();
				htmlBodyString=htmlBodyString.replace("<body>", "");
				htmlBodyString=htmlBodyString.replace("</body>", "");
				htmlBodyString=htmlBodyString.replace("&quot;", "\"");
				
				JsonParser parser = new JsonParser();
				JsonObject contentJSON = (JsonObject)parser.parse(htmlBodyString);
				JsonArray contentProds=(JsonArray)parser.parse(contentJSON.get("prods").toString());
				JsonObject contentTemp=(JsonObject)parser.parse(contentProds.get(0).toString());
//					System.out.println(contentTemp.get("name"));
				return contentTemp;
			}
		});
        searchEbooks.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!searchResults.isVisible())
					searchResults.setVisible(true);
				for(int i=0;i<100;i+=10)
				{
					try {
						Elements links=Jsoup.connect(SearchURL.GoogleSearch+URLEncoder.encode(keywords.getText(), charset)+"&start="+i).userAgent(userAgent).get().select("li.g>h3>a");
						for(Element link:links)
						{
							String title=link.text();
							String linkURL=link.absUrl("href");
						
							linkURL=URLDecoder.decode(linkURL.substring(linkURL.indexOf('=')+1, linkURL.indexOf('&')),"UTF-8");
							if(!linkURL.startsWith("http"))
								continue;
							searchResults.setText(searchResults.getText()+"Title: "+title+"\n");
							searchResults.setText(searchResults.getText()+"Price: "+linkURL+"\n");
						
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
        //This Section is for testing of Web crawler
//        try {
//			Document Html=Jsoup.connect("http://www.amazon.com/Data-Mining-Concepts-Techniques-Management/dp/0123814790").userAgent("Mozilla/5.0").timeout(30000).get();
//			
//			Elements WebContent=Html.select("span.rentPrice");
//			for(Element table:WebContent)
//			{
//				System.out.println(table.html());
//			}
//			
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
        
        
        
        //This Section is for testing of Web crawler
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
        controls.add(searchBooks);
        controls.add(searchEbooks);
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
				new BooksFinder().setVisible(true);				
			}
		});

	}	

}
