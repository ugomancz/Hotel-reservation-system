package cz.muni.fi.pv168.hotel.gui;

import java.util.ResourceBundle;

/**
 * @author Ondrej Kostik
 */
public final class I18N {

    private final ResourceBundle bundle;
    private final String prefix;

    public I18N(Class<?> clazz) {
        var packagePath = clazz.getPackageName().replace(".", "/") + '/';
        bundle = ResourceBundle.getBundle(packagePath + "i18n");
        prefix = clazz.getSimpleName() + ".";
    }

    public String getString(String key) {
        return bundle.getString(prefix + key);
    }

}
