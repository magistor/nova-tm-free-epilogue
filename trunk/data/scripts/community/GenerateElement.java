package community;

/**
 * User: Shaitan
 * Date: 29.12.10
 * Time: 18:49
 */
public class GenerateElement
{
	public static String line(int width, int height)
	{
		String s = "";
		s += "<img src=\"L2UI.SquareWhite\" width=" + width + " height=" + height + ">";
		return s;
	}

	public static String button(String value, String bypass, int width, int height)
	{
		return "<button value=\"" + value + "\" action=\"bypass -h " + bypass + "\" width=" + width + " height=" + height + " back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">";
	}

	public static String buttonTD(String value, String bypass, int width, int height)
	{
		return "<td>" + button(value, bypass, width, height) + "</td>";
	}

	public static String buttonTDTR(String value, String bypass, int width, int height)
	{
		return "<tr>" + buttonTD(value, bypass, width, height) + "</tr>";
	}
}