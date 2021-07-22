package info.androidhive.habadProject.activity;



public  class SavedArticle {

    private String bookName;
    private String articleName;
    private int bookNum;
    private int articleNum;
    private String dateWasMade;
    private String igeretContent;
    private boolean checked = false ;


    public SavedArticle(String bookName , String articleName,String igeretContent, int bookNum , int articleNum ) {
        this.bookName = bookName;
        this.articleName = articleName;
        this.bookNum = bookNum;
        this.dateWasMade = MainClass.dateTextview.getText().toString();
        this.articleNum = articleNum;
        this.igeretContent = igeretContent;
    }
    public String getIgeretContent() {
        return igeretContent;
    }

    public void setIgeretContent(String igeretContent) {
        this.igeretContent = igeretContent;
    }


    public String getBookName() {
        return bookName;
    }

    public String getArticleName() {
        return articleName;
    }

    public int getBookNum() {
        return bookNum;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public void setBookNum(int bookNum) {
        this.bookNum = bookNum;
    }

    public void setArticleNum(int articleNum) {
        this.articleNum = articleNum;
    }

    public int getArticleNum() {
        return articleNum;

    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String toString() {
        return " כרך - "+bookName+", אגרת - "+articleName+". "+" מ - "+dateWasMade ;
    }
    public void toggleChecked() {
        checked = !checked ;
    }
}