package sp;

import java.io.*;

public class MyFileReader
{
    public static Object prognameGet(String path, int Line) throws Exception
    {
        String output = "";
        long Cnt ;
        File f = new File(path);
        if (!f.isFile() || !f.canRead())
        {
            throw new IOException("can't read ");
        }

        BufferedReader br = new BufferedReader(new FileReader(f)); //br is een keer te gebruiken daarna opnieuw maken
        Cnt= br.lines().count();
        br.close();
        br = new BufferedReader(new FileReader(f));
        if(Line <= Cnt)
        {
            try (LineNumberReader lnr = new LineNumberReader(br))
            {
                String line = null;
                int lnum = 0;
                while ((line = lnr.readLine()) != null && (lnum = lnr.getLineNumber()) < Line + 1)
                {
                    boolean empty = "".equals(line);
                    output = (empty ? "empty": line);
                }

            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else
        {
            output = "*";
        }
        br.close();
        return output;
    }
    public static void main(String[] args) throws IOException {}
}
