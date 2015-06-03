package com.ggg.et3.tagutil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.ggg.et3.domain.YearMonth;
import com.ggg.et3.jpa.entity.DCTrans;
import com.ggg.et3.jpa.service.DCTransService;


/**
 * Not used - replaced by CategoryFinder
 * @author gg2712
 *
 */
public class CategoryFinderService {
	
	private static CategoryFinderService instance;
	
	private static final String fileName = "app/assets/data/category-tags.txt";
	private static final String backupFileName = "app/assets/data/category-tags.bakup";
	
	private List<Tag> tagList;
	
	private CategoryFinderService() {
		tagList = new ArrayList<Tag>();
		loadData();
	}

	public static CategoryFinderService getInstance() {
		if(instance == null) {
			synchronized(CategoryFinderService.class) {
				if(instance == null) {
					instance = new CategoryFinderService(); 
				}
			}
		}
		return instance;
	}
	
	private void loadData() {
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
			String line;
			
			int ctr = 0;
			
			while((line = reader.readLine()) != null && line.length() > 0) {
				String[] tokens = line.split("=");
				if (tokens.length > 1) {
					
					try {
						Tag tag = new Tag(Integer.parseInt(tokens[0]), tokens[1]);
						tagList.add(tag);
						ctr++;
					}
					catch(Exception e) {}
				}
			}
			
			System.out.println(ctr + " tags loaded!");
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveData() {
		
		try {
 
			File file = new File(fileName);
 
			if (file.exists()) {
				try { backupFile(); } catch(Exception e) { System.out.println("Error backing up file - " + e.getMessage()); };
			}

			Files.deleteIfExists(Paths.get(fileName));
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (Tag tag : tagList) {
				
				bw.write(tag.getCategoryId() + "=" + tag.getTag());
				bw.newLine();
			
			}
			
			bw.close();
 
			System.out.println("Save tags data done!");
 
		} catch (IOException e) {
			System.out.println("Error saving data :( !!! ");
			e.printStackTrace();
		}
	}
	
	private void backupFile() throws Exception {
		
		Path FROM = Paths.get(fileName);
	    Path TO = Paths.get(backupFileName);
	    
	    System.out.println("About to backup " + fileName + " to " + backupFileName);
	    
		CopyOption[] options = new CopyOption[]{
		      StandardCopyOption.REPLACE_EXISTING,
		      StandardCopyOption.COPY_ATTRIBUTES
		}; 
		
		Files.copy(FROM, TO, options);
	}
	
	public void add(Tag tag) {
		tagList.add(tag);
		saveData();
	}
	
	public void addTag(int categoryId, String tagName) {
		add(new Tag(categoryId, tagName));
	}
	
	public void display() {
		System.out.println("** Tags ****");
		for(Tag tag : tagList) {
			System.out.println(tag);
		}
		System.out.println("************");
	}
	
	public void close() {
		saveData();
	}
	
	public int findCategory(String description) {
		
		int id = -1;
		
		for (Tag tag : tagList) {
			if (description.toLowerCase().contains(tag.getTag().toLowerCase())) {
				id = tag.getCategoryId();
				break;
			}
		}
	
		return id;
	}
	
	/* this class is not thread safe - need to re-design */
	
	public void updateTransactionCategory(YearMonth yearMonth) {

		DCTransService ds = DCTransService.getInstance();
		List<DCTrans> list = ds.findDebits(yearMonth);
		for(DCTrans t : list) {
			int catId = findCategory(t.getDescription());
			if (catId > 0 ) {
				ds.updateCategory(t.getTranId(), catId);
			}
		}
	}
	
	public static void main(String[] args) {
		CategoryFinderService service = new CategoryFinderService();
		
		//Tag tag1 = new Tag(3, "Safeway");
		//Tag tag2 = new Tag(3, "Ralphs");
		//Tag tag3 = new Tag(3, "Sprouts");
		//Tag tag4 = new Tag(3, "Whole Foods"); 
		
		service.display();
		
		//service.add(tag1);
		//service.add(tag2);
		//service.add(tag3);
		//service.add(tag4);
		//service.display();
		
		//System.out.println("Find category " + service.findCategory("kldjadsafewaylml;fk));"));
		//System.out.println("Find category " + service.findCategory("kldjadsfewaylml;fk));"));
		//System.out.println("Find category " + service.findCategory("kldjadRalphs dsa));"));
		
		
		//close() will save the data to a file
		//service.close();
		
	}
}
