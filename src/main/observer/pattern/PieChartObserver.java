package observer.pattern;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;
import observer.CourseRecord;
import observer.LayoutConstants;

/**
 * This class represents a bar chart view of a vector of data. Uses the Observer
 * pattern.
 */
@SuppressWarnings("serial")
public class PieChartObserver extends JPanel implements Observer {
	
	ArrayList<ObserverType> Types = new ArrayList<>();
	
	/**
	 * Creates a BarChartObserver object
	 * 
	 * @param data
	 *            a CourseData object to observe
	 */
	public PieChartObserver(CourseData data) {
		Types.add(ObserverType.CREATE);
        Types.add(ObserverType.UPDATE);
        Types.add(ObserverType.REMOVE);
		data.attach(this);
		this.courseData = data.getUpdate();
		this.setPreferredSize(new Dimension(2 * LayoutConstants.xOffset
				+ (LayoutConstants.barSpacing + LayoutConstants.barWidth)
				* this.courseData.size(), LayoutConstants.graphHeight + 2
				* LayoutConstants.yOffset));
		this.setBackground(Color.white);
	}

	/**
	 * Paint method
	 * 
	 * @param g
	 *            a Graphics object on which to paint
	 */
	public void paint(Graphics g) {
		super.paint(g);
		int radius = 100;
		
		Integer[] data = new Integer[courseData.size()];

		//first compute the total number of students
		double total = 0.0;
		for (int i = 0; i < data.length; i++) {
			data[i] = courseData.get(i).getNumOfStudents();
			total += data[i];
		}
		double startAngle = 0.0;
		for (int i = 0; i < courseData.size(); i++) {
			CourseRecord record = (CourseRecord) courseData.elementAt(i);
			g.setColor(LayoutConstants.courseColours[i]);
			double ratio = (data[i] / total) * 360.0;
			g.fillArc(350, 50, 2 * radius, 2 * radius, (int) startAngle, (int) ratio);
			startAngle += ratio;
			g.drawString(courseData.get(i).getName(),
					LayoutConstants.xOffset + (i + 1)
							* LayoutConstants.barSpacing + i
							* LayoutConstants.barWidth, LayoutConstants.yOffset
							+ LayoutConstants.graphHeight + 20);
			g.setColor(LayoutConstants.courseColours[i]);
			g.drawString(record.getName(),
					LayoutConstants.xOffset + (i + 1)
							* LayoutConstants.barSpacing + i
							* LayoutConstants.barWidth, LayoutConstants.yOffset
							+ LayoutConstants.graphHeight + 20);
		}
	}

	/**
	 * Informs this observer that the observed CourseData object has changed
	 * 
	 * @param o
	 *            the observed CourseData object that has changed
	 */
	public void update(Observable o) {
		CourseData data = (CourseData) o;
		this.courseData = data.getUpdate();

		this.setPreferredSize(new Dimension(2 * LayoutConstants.xOffset
				+ (LayoutConstants.barSpacing + LayoutConstants.barWidth)
				* this.courseData.size(), LayoutConstants.graphHeight + 2
				* LayoutConstants.yOffset));
		this.revalidate();
		this.repaint();
	}
	
	@Override
    public void update(Object o) {
		CourseRecord record = (CourseRecord) o;

        boolean doContain = false;
        for (CourseRecord courseRecord : courseData)
            if (courseRecord.getName().equals(record.getName())){
                courseRecord.setNumOfStudents(record.getNumOfStudents());
                doContain = true;
            }

        if (!doContain)
            courseData.add(record);

        this.revalidate();
        this.repaint();
    }
	
	@Override
    public ArrayList<ObserverType> getTypes() {
        return Types;
    }

	private Vector<CourseRecord> courseData;
}