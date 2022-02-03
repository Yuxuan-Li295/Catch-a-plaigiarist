import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class DocumentsProcessorTest {
	DocumentsProcessor processor = new DocumentsProcessor();
	Map<String, List<String>> testmap = new HashMap<>();
	List<String> testlist = new ArrayList<>();
	List<Tuple<String, Integer>> tuplelistTest = new ArrayList<>();
	TreeSet<Similarities> treeset2 = new TreeSet<>();
	
	@org.junit.Test
	public void ProcessDocuments() {
			testlist.add("thisis");
			testlist.add("isa");
			testlist.add("atest");
			testlist.add("testdocument");
			testmap = processor.processDocuments("/autograder/submission/test files", 2);
			assertEquals(testlist, testmap.get("/autograder/submission/file1.txt"));
		}
	
	@org.junit.Test
	public void teststoreNWordSequences1() {
		testmap = processor.processDocuments("/autograder/submission/test files", 4);
		tuplelistTest = processor.storeNWordSequences(testmap, "/autograder/submission/nwordFilePath.txt");
		int size = 0;
		for(Tuple<String, Integer> tuple: tuplelistTest) {
			if(tuple.getLeft().equals("/autograder/submission/file1.txt")) {
			 size = tuple.getRight();
			}
		}
		assertEquals(28, size);
	}
	
	@org.junit.Test
	public void computeSimilarities() {
		testmap = processor.processDocuments("test files 2", 3);
		tuplelistTest = processor.storeNWordSequences(testmap, "/autograder/submission/nwordFilePath.txt");	
		treeset2 = processor.computeSimilarities("/autograder/submission/nwordFilePath.txt",tuplelistTest);
		for(Similarities sb : treeset2)
		{
			
			if(sb.getFile1().equals("/autograder/submission/file5.txt")&& sb.getFile2().equals("/autograder/submission/file4.txt")) {
				assertEquals(3, sb.getCount());
				
			}
		}
	}
	@org.junit.Test
	public void printSimilarities() {
		Similarities s1 = new Similarities("/autograder/submission/file1.txt", "/autograder/submission/file2.txt");
		s1.setCount(1);
		treeset2.add(s1);
		Similarities s2 = new Similarities("/autograder/submission/file3.txt", "/autograder/submission/file2.txt");
		s1.setCount(3);
		treeset2.add(s2);
		processor.printSimilarities(treeset2, 1);
		int n = treeset2.size(); 
        List<Similarities> list = new ArrayList<>();
        Similarities arr[] = new Similarities[2];
        int i = 0;
        for (Similarities s : treeset2) 
            arr[i++] = s; 
        assertEquals(s2,arr[0]);
    
	}
	@org.junit.Test
	public void processAndStore() {
		tuplelistTest = processor.processAndStore("/autograder/submission/test files", "/autograder/submission/sequenceFile.txt", 4);
		int size = 0;
		for(Tuple<String, Integer> tuple: tuplelistTest) {
			if(tuple.getLeft().equals("/autograder/submission/file1.txt")) {
			 size = tuple.getRight();
			}
		}
		assertEquals(28, size);
	}
	
}
