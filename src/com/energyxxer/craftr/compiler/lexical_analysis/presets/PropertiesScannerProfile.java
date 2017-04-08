package com.energyxxer.craftr.compiler.lexical_analysis.presets;

import com.energyxxer.craftr.compiler.lexical_analysis.profiles.ScannerContext;
import com.energyxxer.craftr.compiler.lexical_analysis.profiles.ScannerContextResponse;
import com.energyxxer.craftr.compiler.lexical_analysis.profiles.ScannerProfile;
import com.energyxxer.craftr.compiler.lexical_analysis.token.Token;

import java.util.ArrayList;

import static com.energyxxer.craftr.compiler.lexical_analysis.presets.PropertiesScannerProfile.Stage.*;

/**
 * Created by User on 4/8/2017.
 */
public class PropertiesScannerProfile extends ScannerProfile {

    enum Stage {
        COMMENT, KEY, SEPARATOR, VALUE
    }

    private Stage stage = KEY;

    /**
     * Creates a JSON Analysis Profile.
     * */
    public PropertiesScannerProfile() {
        ScannerContext propertyContext = str -> {
            if(str.trim().length() <= 0) return new ScannerContextResponse(false);
            if(str.startsWith("\n")) {
                stage = KEY;
                return new ScannerContextResponse(false);
            }
            if(stage == KEY) {
                if(str.trim().startsWith("#")) {
                    StringBuilder comment = new StringBuilder();
                    for(char ch : str.toCharArray()) {
                        if(ch == '\n') break;
                        comment.append(ch);
                    }
                    return new ScannerContextResponse(true, comment.toString(), COMMENT.name());
                } else {
                    StringBuilder key = new StringBuilder();
                    for(char ch : str.toCharArray()) {
                        if(ch == '=') {
                            stage = SEPARATOR;
                            break;
                        } else if(ch == '\n') {
                            break;
                        } else key.append(ch);
                    }
                    return new ScannerContextResponse(true, key.toString(), KEY.name());
                }
            } else if(stage == SEPARATOR) {
                stage = VALUE;
                return new ScannerContextResponse(true, "=", SEPARATOR.name());
            } else if(stage == VALUE) {
                StringBuilder value = new StringBuilder();
                for(char ch : str.toCharArray()) {
                    if(ch == '\n') {
                        break;
                    } else value.append(ch);
                }
                stage = KEY;
                return new ScannerContextResponse(true, value.toString(), VALUE.name());
            }
            return null;
        };

        ArrayList<ScannerContext> propertiesContexts = new ArrayList<>();
        propertiesContexts.add(propertyContext);
        this.contexts = propertiesContexts;
    }

    @Override
    public void putHeaderInfo(Token header) {
        header.attributes.put("TYPE","properties");
        header.attributes.put("DESC","Java Properties File");
    }
}
