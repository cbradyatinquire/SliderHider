package speed;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.DefaultCommand;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.LogoException;
import org.nlogo.api.PrimitiveManager;
import org.nlogo.app.App;
import org.nlogo.lite.Applet;
import org.nlogo.nvm.ExtensionContext;
import org.nlogo.window.GUIWorkspace;
import org.nlogo.window.SpeedSliderPanel;
import org.nlogo.workspace.AbstractWorkspace;


public class SliderHider extends DefaultClassManager {

	static JPanel thepanel = null;
	static Boolean isApplet;
	
	
	@Override
	public void load(PrimitiveManager prim) throws ExtensionException {

		isApplet = AbstractWorkspace.isApplet();
		
		
		prim.addPrimitive("hide-slider", new HideSlider());
		prim.addPrimitive("show-slider", new ShowSlider());
	}
	
	
	//walk to the top of the GUI from the Context, and then back down for the speedslider.
	private static JPanel getPanel(Context ctxt)
	{
		if (thepanel == null)
		{
			if (isApplet)
			{
				ExtensionContext ec = ((ExtensionContext)ctxt);
				GUIWorkspace gw = (GUIWorkspace)ec.workspace();
				Container c = (Container)gw.getWidgetContainer();
				while (! (c instanceof Applet) )
				{
					c = c.getParent();
				}
				c = ((Applet)c).getContentPane();			
				thepanel = CheckComponent(c);
			}
			else
			{
				GUIWorkspace gw = App.app().workspace();
				Container c = (Container)gw.getWidgetContainer();
				while (! (c instanceof JFrame) )
				{
					c = c.getParent();
				}
				c = ((JFrame)c).getContentPane();
				thepanel = CheckComponent(c);
			}
		}
		return thepanel;
	}
	
	
	//recursively look down the hierarchy for the SpeedSlider.
	private static SpeedSliderPanel CheckComponent(Component co)
	{
		int i = 0;
		if ( co instanceof Container )
		{
			Container c = (Container)co;
			while (i < c.getComponentCount())
			{
				if (c.getComponent(i) instanceof SpeedSliderPanel)
					return (SpeedSliderPanel) c.getComponent(i);
				SpeedSliderPanel x = CheckComponent(c.getComponent(i));
				if ( x != null)
					return x;
				i++;
			}
		}
		return null;
	}
	
	
	
	public static class ShowSlider extends DefaultCommand {
		@Override
		public void perform(Argument[] arg0, Context ctxt)
				throws ExtensionException, LogoException {
			
			getPanel(ctxt).setVisible( true );
			
		}
	}
	
	public static class HideSlider extends DefaultCommand {

		@Override
		public void perform(Argument[] arg0, Context ctxt)
				throws ExtensionException, LogoException {
			
			getPanel(ctxt).setVisible(false);	
		}
	}
	
	
	
}
