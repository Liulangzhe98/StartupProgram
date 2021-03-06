package sp;

public class Program
{
    private String name, path, url;


    @Override
    public String toString()
    {
        return name + "  "+ path;
    }


    public Program(){
        this.name = "";
        this.path = "";
        this.url = "";
    }


    public Program(String name, String path, String url){
        this.name = name;
        this.path = path;
        if(url.contains("not set"))
        {
            url = "";
        }


        else
        {
            this.url = url;
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public static Program with(String path, String name, String url, String selected)
    {
        return new Program(path, name, url);
    }
}