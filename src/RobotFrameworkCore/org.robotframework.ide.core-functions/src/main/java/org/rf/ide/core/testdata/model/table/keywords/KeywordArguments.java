/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.model.table.keywords;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rf.ide.core.testdata.model.AModelElement;
import org.rf.ide.core.testdata.model.FilePosition;
import org.rf.ide.core.testdata.model.ICommentHolder;
import org.rf.ide.core.testdata.model.ModelType;
import org.rf.ide.core.testdata.text.read.recognizer.RobotToken;
import org.rf.ide.core.testdata.text.read.recognizer.RobotTokenType;

public class KeywordArguments extends AModelElement<UserKeyword> implements ICommentHolder, Serializable {

    private static final long serialVersionUID = 2104462542479156388L;

    private final RobotToken declaration;

    private final List<RobotToken> arguments = new ArrayList<>();

    private final List<RobotToken> comment = new ArrayList<>();

    public KeywordArguments(final RobotToken declaration) {
        fixForTheType(declaration, RobotTokenType.KEYWORD_SETTING_ARGUMENTS, true);
        this.declaration = declaration;
    }

    @Override
    public boolean isPresent() {
        return (declaration != null);
    }

    @Override
    public RobotToken getDeclaration() {
        return declaration;
    }

    public List<RobotToken> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    public void addArgument(final RobotToken argument) {
        fixForTheType(argument, RobotTokenType.KEYWORD_SETTING_ARGUMENT);
        arguments.add(argument);
    }
    
    public void addArgument(final int index, final String arg) {
        updateOrCreateTokenInside(arguments, index, arg, RobotTokenType.KEYWORD_SETTING_ARGUMENT);
    }

    @Override
    public List<RobotToken> getComment() {
        return Collections.unmodifiableList(comment);
    }

    @Override
    public void addCommentPart(final RobotToken rt) {
        fixComment(getComment(), rt);
        this.comment.add(rt);
    }

    @Override
    public ModelType getModelType() {
        return ModelType.USER_KEYWORD_ARGUMENTS;
    }

    @Override
    public FilePosition getBeginPosition() {
        return getDeclaration().getFilePosition();
    }

    @Override
    public List<RobotToken> getElementTokens() {
        final List<RobotToken> tokens = new ArrayList<>();
        if (isPresent()) {
            tokens.add(getDeclaration());
            tokens.addAll(getArguments());
            tokens.addAll(getComment());
        }

        return tokens;
    }

    @Override
    public void setComment(final String comment) {
        final RobotToken tok = new RobotToken();
        tok.setText(comment);
        setComment(tok);
    }

    @Override
    public void setComment(final RobotToken comment) {
        this.comment.clear();
        addCommentPart(comment);
    }

    @Override
    public void removeCommentPart(final int index) {
        this.comment.remove(index);
    }

    @Override
    public void clearComment() {
        this.comment.clear();
    }

    @Override
    public boolean removeElementToken(final int index) {
        return super.removeElementFromList(arguments, index);
    }
    
    public KeywordArguments copy() {
        final KeywordArguments keywordArgs = new KeywordArguments(this.getDeclaration().copyWithoutPosition());
        for (final RobotToken arg : getArguments()) {
            keywordArgs.addArgument(arg.copyWithoutPosition());
        }
        for (final RobotToken commentToken : getComment()) {
            keywordArgs.addCommentPart(commentToken.copyWithoutPosition());
        }
        return keywordArgs;
    }
}
