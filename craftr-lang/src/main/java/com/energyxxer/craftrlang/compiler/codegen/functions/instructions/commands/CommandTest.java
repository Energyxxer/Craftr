package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands;

import com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.selector.Selector;
import com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.selector.TagArgument;
import com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.selector.TypeArgument;

public class CommandTest {
    public static void main(String[] args) {
        Selector selector = new Selector(Selector.BaseSelector.ALL_ENTITIES);

        selector.addArguments(new TypeArgument("bat"), new TagArgument("a"), new TagArgument("!b"));

        System.out.println("selector = " + selector);
    }
}
