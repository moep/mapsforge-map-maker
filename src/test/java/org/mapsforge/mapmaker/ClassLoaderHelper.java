package org.mapsforge.mapmaker;

import java.lang.reflect.Field;

import org.mapsforge.mapmaker.gui.MapFileWizardPage;


/**
 * This class helps accessing private methods and fields via reflection calls.
 * @author Karsten Groll
 *
 */
public class ClassLoaderHelper {
	static Object getStaticFieldFromClass(Class<? extends Object> clazz, String fieldName) {
		Field f;
		Object o = null;
		try {
			f = clazz.getDeclaredField(fieldName);
			f.setAccessible(true);
			o = f.get(null);			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return o;
	}
	
	public static void main(String[] args) {
		Object o = getStaticFieldFromClass(MapFileWizardPage.class, "SETTINGS_SECTION_NAME");
		String s = (String)null;
		System.out.println("String: " + s);
	}
}
