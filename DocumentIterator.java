import java.io.IOException; 
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DocumentIterator implements Iterator<String> {

    private Reader r;
    private int c = -1;
    private int n;

    public DocumentIterator(Reader r, int n) {
        this.r = r;
        this.n = n;
        skipNonLetters();
    }

    private void skipNonLetters() {
        try {
            this.c = this.r.read();
            while (!Character.isLetter(this.c) && this.c != -1) {
                this.c = this.r.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String next() {

        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        String answer = "";
		try {
            answer = "";
            for (int i = 0; i < n; i++) {
                while (Character.isLetter(this.c)) {
                    if (Character.isUpperCase((char) this.c)) {
                        this.c = Character.toLowerCase((char) this.c);
                    }
                    answer += (char) this.c;
                    this.c = this.r.read();
                }
                if (i == 0) {
                    this.r.mark(100);
                }
                skipNonLetters();
            }
            this.r.reset();
            this.c = this.r.read();

        } catch (IOException e) {
            throw new NoSuchElementException();
        }

        return answer;

    }

    @Override
    public boolean hasNext() {
        int temp = this.c;
        try {
            this.r.mark(100);
        } catch (Exception e) {
        }
        int counter = 0;
        while (this.c != -1) {
            while (Character.isLetter(this.c)) {
				try {
                    this.c = this.r.read();
                } catch (IOException e) {
					// TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            skipNonLetters();
            counter++;

            if (counter == this.n) {
                try {
                    this.r.reset();
                    this.c = temp;
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        try {
            this.r.reset();
            this.c = temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
