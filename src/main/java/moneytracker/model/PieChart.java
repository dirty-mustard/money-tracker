package moneytracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PieChart {

    private List<Point> points = new ArrayList<>();

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public static class Point {

        private String name;

        @JsonProperty("y")
        private BigDecimal value;

        private String color;

        public Point() {
            // Empty constructor
        }

        public Point(String name, BigDecimal value, String color) {
            this.name = name;
            this.value = value;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

}
