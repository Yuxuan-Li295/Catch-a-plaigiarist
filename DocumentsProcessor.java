import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class DocumentsProcessor implements IDocumentsProcessor {

    @Override
    public Map<String, List<String>> processDocuments(String directoryPath, int n) {
        Map<String, List<String>> processMap = new HashMap<>();
 
        try {
            File folder = new File(directoryPath);
            File[] filesinfolder = folder.listFiles();

            for (int i = 0; i < filesinfolder.length; i++) {
                List<String> processList = new ArrayList<>();
                if (filesinfolder[i].isFile() && filesinfolder[i].getName()
                    .substring(filesinfolder[i].getName().length() - 4).equals(".txt")) {
                    BufferedReader reader = new BufferedReader(new FileReader(filesinfolder[i]));
                    DocumentIterator iterator = new DocumentIterator(reader, n);

                    while (iterator.hasNext()) {
                        processList.add(iterator.next());
                    }
                    processList.remove(processList.size() - 1);
                    processMap.put(filesinfolder[i].getName(), processList);

                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return processMap;
    }

    @Override
    public List<Tuple<String, Integer>> storeNWordSequences(
        Map<String, List<String>> docs, String nwordFilePath) {
        Tuple<String, Integer> tuple;
        List<Tuple<String, Integer>> tuplelist = new ArrayList<>();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(nwordFilePath));
            for (String key : docs.keySet()) {
                String nWordSquence = String.join(" ", docs.get(key)) + " ";

                int bytelength = nWordSquence.getBytes().length;
                tuple = new Tuple<String, Integer>(key, bytelength);
                tuplelist.add(tuple);
                writer.write(nWordSquence);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tuplelist;
    }

    @Override
    public TreeSet<Similarities> computeSimilarities(
        String nwordFilePath, List<Tuple<String, Integer>> fileindex) {
        int current = 0;
        RandomAccessFile raf;
        StringBuilder sb = new StringBuilder("");
        Map<String, List<String>> wordmap = new HashMap<>();
        Comparator<Similarities> comp = new Comparator<Similarities>() {
            @Override
            public int compare(Similarities o1, Similarities o2) {
                if (o1.getFile1().equals(o2.getFile1()) && o1.getFile2().equals(o2.getFile2())) {
                    return 0;
                }
                if (o1.getFile1().equals(o2.getFile2()) && o1.getFile2().equals(o2.getFile1())) {
                    return 0;
                } else {
                    return -1;
                }
            }
        };
        TreeSet<Similarities> treeset = new TreeSet<>(comp);
        try {
            raf = new RandomAccessFile(nwordFilePath, "r");
            try {
                for (int j = 0; j < fileindex.size(); j++) {
                    int index = fileindex.get(j).getRight();
                    String filename = fileindex.get(j).getLeft();

                    for (int i = 0; i < index; i++) {

                        current = raf.readByte();

                        if ((char) current == ' ') {

                            if (wordmap.containsKey(sb.toString())) {
                                if (!wordmap.get(sb.toString()).contains(filename)) {
                                    String string = sb.toString();
                                    for (int z = 0; z < wordmap.get(string).size(); z++) {

                                        String prevfile = wordmap.get(sb.toString()).get(z);
                                        Similarities similarity = 
                                                new Similarities(prevfile, filename);
                                        if (!treeset.contains(similarity)) {

                                            treeset.add(similarity);
                                            similarity.setCount(1);
                                        } else {
                                            Similarities s = 
                                                (Similarities) ((TreeSet<Similarities>) treeset)
                                                .ceiling(similarity);
                                            s.setCount((int) s.getCount() + 1);
                                        }
                                    }
                                    wordmap.get(sb.toString()).add(filename);
                                }
                                sb.setLength(0);
                            } else {
                                List<String> filelist = new ArrayList<>();
                                filelist.add(filename);
                                wordmap.put(sb.toString(), filelist);
                                sb.setLength(0);
                            }
                        } else {
                            sb.append((char) current);
                        }

                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return treeset;
    }

    @Override
    public void printSimilarities(TreeSet<Similarities> sims, int threshold) {

        Comparator<Similarities> comp = new Comparator<Similarities>() {
            @Override
            public int compare(Similarities o1, Similarities o2) {
                if (o1.getCount() == o2.getCount()) {
                    return o1.getFile1().compareTo(o2.getFile1());
                } else {
                    return o2.getCount() - o1.getCount();
                }

            }
        };

        TreeSet<Similarities> finalSet = new TreeSet<>(comp);
        finalSet.addAll(sims);
        for (Similarities s : finalSet) {
            if (s.getCount() > threshold) {
                System.out.println(s.getFile1() + " " + s.getFile2() + " " + s.getCount());

            }
        }

    }

    @Override
    public List<Tuple<String, Integer>> processAndStore(
        String directoryPath, String sequenceFile, int n) {
        List<Tuple<String, Integer>> tuplelist2 = new ArrayList<>();
        StringBuilder file = new StringBuilder("");
        try {
            File folder2 = new File(directoryPath);
            File[] filesinfolder2 = folder2.listFiles();
            BufferedWriter writer2 = new BufferedWriter(new FileWriter(sequenceFile));
            
            for (int i = 0; i < filesinfolder2.length; i++) {
                File current = filesinfolder2[i];
                String filename = current.getName();
                int namelength = filename.length();
                if (current.isFile() && current.getName()
                    .substring(namelength - 4).equals(".txt")) {
                    BufferedReader reader5 = new BufferedReader(new FileReader(current), n);
                    DocumentIterator iterator = new DocumentIterator(reader5, n);
                    while (iterator.hasNext()) {
                        file.append(iterator.next() + " ");
                    }

                    reader5.close();
                }
                Tuple<String, Integer> tuple = new Tuple<>(filename, file.toString().length());
                tuplelist2.add(tuple);
                writer2.write(file.toString());
                file.setLength(0);
            }
            writer2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tuplelist2;

    }

}
