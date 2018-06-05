package info.upump.questionnairehis.entity;

public class Interval {
    private String category;
    private int start;
    private int finish;

    public Interval() {
    }

    public Interval(int start, int finish, String category) {
        this.start = start;
        this.finish = finish;
        this.category = category;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "category='" + category + '\'' +
                ", start=" + start +
                ", finish=" + finish +
                '}';
    }
}
