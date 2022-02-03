//import java.util.ArrayList;
//import java.util.List;

/**
 * @author ericfouh
 */
public class Similarities implements Comparable<Similarities> {
    /**
     * 
     */
    private String file1;
    private String file2;
    private int    count;


    /**
     * @param file1
     * @param file2
     */
    public Similarities(String file1, String file2) {
        this.file1 = file1;
        this.file2 = file2;
        this.setCount(0);
    }


    /** 
     * @return the file1
     */
    public String getFile1() {
        return file1;
    }


    /**
     * @return the file2
     */
    public String getFile2() {
        return file2;
    }


    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }


    /**
     * @param count the count to set
     */
    public void setCount(int count) {

        this.count = count;
    }


    @Override
    public int compareTo(Similarities o) {
        //TODO
        if (this.getFile1() == o.getFile1() && this.getFile2() == o.getFile2()) {
            return 0;
        }
        if (this.getFile1() == o.getFile2() && this.getFile2() == o.getFile1()) {
            return 0;
        } else {
            return -1;
        }
    }

}
