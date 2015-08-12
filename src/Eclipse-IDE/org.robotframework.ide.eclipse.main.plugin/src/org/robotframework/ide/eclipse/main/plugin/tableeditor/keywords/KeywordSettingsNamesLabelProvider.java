package org.robotframework.ide.eclipse.main.plugin.tableeditor.keywords;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.Stylers;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.robotframework.ide.eclipse.main.plugin.RedImages;
import org.robotframework.ide.eclipse.main.plugin.model.RobotDefinitionSetting;
import org.robotframework.ide.eclipse.main.plugin.tableeditor.ISectionFormFragment.MatchesProvider;
import org.robotframework.ide.eclipse.main.plugin.tableeditor.MatchesHighlightingLabelProvider;
import org.robotframework.red.graphics.ColorsManager;
import org.robotframework.red.graphics.ImagesManager;

public class KeywordSettingsNamesLabelProvider extends MatchesHighlightingLabelProvider {

    private final Map<String, String> tooltips = new LinkedHashMap<>();
    {
        tooltips.put("Teardown", "The keyword %s is executed after every other keyword inside the definition");
        tooltips.put("Timeout", "Specifies maximum time this keyword is allowed to execute before being aborted.\n"
                + "This setting overrides Test Timeout setting set on suite level\n"
                + "Numerical values are intepreted as seconds but special syntax like '1min 15s' or '2 hours' can be used.");
        tooltips.put("Return", "Specify the return value for this keyword. Multiple values can be used.");
    }

    public KeywordSettingsNamesLabelProvider(final MatchesProvider matchesProvider) {
        super(matchesProvider);
    }

    @Override
    public Color getBackground(final Object element) {
        return getSetting(element) == null ? ColorsManager.getColor(250, 250, 250) : null;
    }

    @Override
    public StyledString getStyledText(final Object element) {
        final RobotDefinitionSetting setting = getSetting(element);
        if (setting == null) {
            return highlightMatches(new StyledString(getSettingName(element), Stylers.withForeground(200, 200, 200)));
        } else {
            return highlightMatches(new StyledString(setting.getName()));
        }
    }

    @Override
    public String getToolTipText(final Object element) {
        final Entry<String, RobotDefinitionSetting> entry = getEntry(element);
        final RobotDefinitionSetting setting = getSetting(element);
        final String keyword = setting == null ? "given in first argument" : getKeyword(setting);

        return String.format(tooltips.get(entry.getKey()), keyword);
    }

    private String getKeyword(final RobotDefinitionSetting element) {
        final List<String> arguments = element.getArguments();
        return arguments.isEmpty() ? "<empty>" : "'" + arguments.get(0) + "'";
    }

    @Override
    public Image getToolTipImage(final Object object) {
        return ImagesManager.getImage(RedImages.getTooltipImage());
    }

    @SuppressWarnings("unchecked")
    private Entry<String, RobotDefinitionSetting> getEntry(final Object element) {
        return (Entry<String, RobotDefinitionSetting>) element;
    }

    private String getSettingName(final Object element) {
        return (String) ((Entry<?, ?>) element).getKey();
    }

    private RobotDefinitionSetting getSetting(final Object element) {
        return (RobotDefinitionSetting) ((Entry<?, ?>) element).getValue();
    }
}