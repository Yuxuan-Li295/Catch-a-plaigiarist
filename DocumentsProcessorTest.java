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
	String dir1 = "/autograder/submission/test_files";
	String dir2 = "/autograder/submission/test_files_2";
	String file1 = "/autograder/submission/file1.txt";
	String file2 = "/autograder/submission/file2.txt"; 
	String file3 = "/autograder/submission/file3.txt";
	String file4 = "/autograder/submission/file4.txt";
	String file5 = "/autograder/submission/file5.txt";
	String nwordFilePath = "nwordFilePath.txt";
	String sequenceFile = "sequenceFile.txt";

	@org.junit.Test
	public void ProcessDocuments() {
		testlist.add("thisis");
		testlist.add("isa");
		testlist.add("atest");
		testlist.add("testdocument");
		testmap = processor.processDocuments(dir1, 2);
		assertEquals(testlist, testmap.get(file1));
	}

	@org.junit.Test
	public void teststoreNWordSequences1() {
		testmap = processor.processDocuments(dir1, 4);
		tuplelistTest = processor.storeNWordSequences(testmap, nwordFilePath);
		int size = 0;
		for (Tuple<String, Integer> tuple : tuplelistTest) {
			if (tuple.getLeft().equals(file1)) {
				size = tuple.getRight();
			}
		}
		assertEquals(28, size);
	}

	@org.junit.Test
	public void computeSimilarities() {
		testmap = processor.processDocuments(dir2, 3);
		tuplelistTest = processor.storeNWordSequences(testmap, nwordFilePath);
		treeset2 = processor.computeSimilarities(nwordFilePath, tuplelistTest);
		for (Similarities sb : treeset2) {

			if (sb.getFile1().equals(file5) && sb.getFile2().equals(file4)) {
				assertEquals(3, sb.getCount());

			}
		}
	}

	@org.junit.Test
	public void printSimilarities() {
		Similarities s1 = new Similarities(file1, file2);
		s1.setCount(1);
		treeset2.add(s1);
		Similarities s2 = new Similarities(file3, file2);
		s1.setCount(3);
		treeset2.add(s2);
		processor.printSimilarities(treeset2, 1);
		Similarities arr[] = new Similarities[2];
		int i = 0;
		for (Similarities s : treeset2)
			arr[i++] = s;
		assertEquals(s2, arr[0]);

	}

	@org.junit.Test
	public void processAndStore() {
		tuplelistTest = processor.processAndStore(dir1, sequenceFile, 4);
		int size = 0;
		for (Tuple<String, Integer> tuple : tuplelistTest) {
			if (tuple.getLeft().equals(file1)) {
				size = tuple.getRight();
			}
		}
		assertEquals(28, size);
	}

}
