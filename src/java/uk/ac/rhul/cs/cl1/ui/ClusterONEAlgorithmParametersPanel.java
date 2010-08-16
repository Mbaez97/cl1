package uk.ac.rhul.cs.cl1.ui;

import java.awt.Component;
import java.util.TreeMap;

import info.clearthought.layout.TableLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import uk.ac.rhul.cs.cl1.ClusterONEAlgorithmParameters;

/**
 * Component that lets the user adjust the algorithm parameters of Cluster ONE
 * 
 * @author ntamas
 */
public class ClusterONEAlgorithmParametersPanel extends JPanel {
	/** Sections used in this panel */
	public enum Section {
		BASIC("Basic parameters"),
		ADVANCED("Advanced parameters");
		
		/** Title of the section */
		protected String title;
		
		Section(String title) {
			this.title = title;
		}
		
		/** Returns the title of the section */
		public String getTitle() { return this.title; }
	}

	/** Subpanel components corresponding to each panel */
	protected TreeMap<Section, JPanel> subpanels = null;
	
	/** Spinner component for adjusting the minimum cluster size */
	protected JSpinner minimumClusterSizeSpinner;
	
	/** Spinner component for adjusting the minimum cluster density */
	protected JSpinner minimumClusterDensitySpinner;
	
	/** Spinner component for selecting the haircut threshold */
	protected JSpinner haircutThresholdSpinner;
	
	/** Combobox for selecting the cluster merging method */
	protected JComboBox mergingMethodCombo;
	
	/** Spinner component for adjusting the overlap threshold of the merging step */
	protected JSpinner overlapThresholdSpinner;
	
	/** Combobox for selecting the seeding method */
	protected JComboBox seedMethodCombo;
	
	/** Seeding methods */
	protected String[] seedMethods = {"From unused nodes", "From every node", "From every edge"};
	
	/** Merging methods */
	protected String[] mergingMethods = {"Match coefficient", "Meet/min coefficient"};

	public ClusterONEAlgorithmParametersPanel() {
		super();
		
		ClusterONEAlgorithmParameters defaultParams =
			new ClusterONEAlgorithmParameters();
		
		subpanels = new TreeMap<Section, JPanel>();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		/* Minimum cluster size spinner */	
		minimumClusterSizeSpinner = new JSpinner();
		minimumClusterSizeSpinner.setModel(
				new SpinnerNumberModel(defaultParams.getMinSize(),
						1, Integer.MAX_VALUE, 1)
		);
		((JSpinner.NumberEditor)minimumClusterSizeSpinner.getEditor()).getTextField().setColumns(5);
		this.addComponent(Section.BASIC, "Minimum size:", minimumClusterSizeSpinner);
		
		/* Minimum cluster density spinner */
		minimumClusterDensitySpinner = new JSpinner();
		minimumClusterDensitySpinner.setModel(
				new SpinnerNumberModel(defaultParams.getMinDensity(),
						0.0, 1.0, 0.05)
		);
		((JSpinner.NumberEditor)minimumClusterDensitySpinner.getEditor()).getTextField().setColumns(5);
		this.addComponent(Section.BASIC, "Minimum density:", minimumClusterDensitySpinner);
		
		/* Haircut threshold spinner */
		haircutThresholdSpinner = new JSpinner();
		haircutThresholdSpinner.setModel(
				new SpinnerNumberModel(defaultParams.getHaircutThreshold(),
						0.0, 1.0, 0.05)
		);
		((JSpinner.NumberEditor)haircutThresholdSpinner.getEditor()).getTextField().setColumns(5);
		this.addComponent(Section.ADVANCED, "Haircut threshold:", haircutThresholdSpinner);
		
		/* Merging method combobox */
		mergingMethodCombo = new JComboBox(mergingMethods);
		this.addComponent(Section.ADVANCED, "Merging method:", mergingMethodCombo);
		
		/* Overlap threshold spinner */
		overlapThresholdSpinner = new JSpinner();
		overlapThresholdSpinner.setModel(
				new SpinnerNumberModel(defaultParams.getOverlapThreshold(),
						0.0, 1.0, 0.05)
		);
		((JSpinner.NumberEditor)overlapThresholdSpinner.getEditor()).getTextField().setColumns(5);
		this.addComponent(Section.BASIC, "Overlap threshold:", overlapThresholdSpinner);
		
		/* Seed selection method */
		seedMethodCombo = new JComboBox(seedMethods);
		this.addComponent(Section.ADVANCED, "Seeding method:", seedMethodCombo);
	}

	/**
	 * Returns a {@link ClusterONEAlgorithmParameters} object from the current state
	 * of the panel
	 */
	public ClusterONEAlgorithmParameters getParameters() {
		ClusterONEAlgorithmParameters result = new ClusterONEAlgorithmParameters();
		
		result.setFluffClusters(true);
		
		result.setMinSize((Integer)minimumClusterSizeSpinner.getValue());
		result.setMinDensity((Double)minimumClusterDensitySpinner.getValue());
		result.setHaircutThreshold((Double)haircutThresholdSpinner.getValue());
		result.setOverlapThreshold((Double)overlapThresholdSpinner.getValue());
		
		try {
			if (seedMethodCombo.getSelectedIndex() == 0)
				result.setSeedGenerator("unused_nodes");
			else if (seedMethodCombo.getSelectedIndex() == 1)
				result.setSeedGenerator("nodes");
			else if (seedMethodCombo.getSelectedIndex() == 2)
				result.setSeedGenerator("edges");
			else
				return null;
		} catch (InstantiationException ex) {
			return null;
		}
		
		if (mergingMethodCombo.getSelectedIndex() == 0)
			result.setMergingMethod("match");
		else if (mergingMethodCombo.getSelectedIndex() == 1)
			result.setMergingMethod("meet/min");
		else
			return null;

		return result;
	}
	
	/**
	 * Returns the subpanel corresponding to the given section
	 */
	public JPanel getSubpanel(Section section) {
		if (subpanels.containsKey(section))
			return subpanels.get(section);
		
		JPanel newPanel = constructNewSubpanel(section.getTitle());
		
		double sizes[][] = {
				{TableLayout.PREFERRED, 10, TableLayout.PREFERRED},
				{TableLayout.PREFERRED}
		};
		newPanel.setLayout(new TableLayout(sizes));
		this.add(newPanel);
		subpanels.put(section, newPanel);
		
		return newPanel;
	}
	
	/**
	 * Constructs a new subpanel with the given title
	 */
	protected JPanel constructNewSubpanel(String title) {
		JPanel newPanel = new JPanel();
		newPanel.setBorder(BorderFactory.createTitledBorder(title));
		return newPanel;
	}
	
	/**
	 * Adds a new component to the end of the parameters panel
	 * @param section    the section to add the component to
	 * @param caption    caption of the component in the left column
	 * @param component  the component itself that should go in the right column
	 */
	public void addComponent(Section section, String caption, Component component) {
		JLabel label = new JLabel(caption);
		JPanel subpanel = this.getSubpanel(section);
		TableLayout layout = (TableLayout)subpanel.getLayout();
		
		int numRows = layout.getNumRow();
		layout.insertRow(numRows, TableLayout.PREFERRED);
		subpanel.add(label, "0, "+numRows+", r, c");
		subpanel.add(component, "2, "+numRows+", l, c");
	}
	
	/**
	 * Adds a new component to the Basic parameters section of the panel
	 * @param caption    caption of the component in the left column
	 * @param component  the component itself that should go in the right column
	 */
	public void addComponent(String caption, Component component) {
		addComponent(Section.BASIC, caption, component);
	}
}
