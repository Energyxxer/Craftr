package com.energyxxer.craftrlang.compiler;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Energyxxer on 07/30/2017.
 */
public interface LibraryLoad {
    void onLoad(HashMap<File, TokenPattern<?>> patterns);
}
