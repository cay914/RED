/*
 * Copyright 2016 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.refactoring;

import java.util.List;
import java.util.Optional;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.DocumentChange;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.robotframework.red.swt.SwtThread;
import org.robotframework.red.swt.SwtThread.Evaluation;

/**
 * @author Michal Anglart
 */
class RedXmlInTextEditorChangesCollector {

    private final IFile redXmlFile;

    private final Optional<IPath> pathAfterRefactoring;

    private final IPath pathBeforeRefactoring;

    RedXmlInTextEditorChangesCollector(final IFile redXmlFile, final IPath pathBeforeRefactoring,
            final Optional<IPath> pathAfterRefactoring) {
        this.redXmlFile = redXmlFile;
        this.pathBeforeRefactoring = pathBeforeRefactoring;
        this.pathAfterRefactoring = pathAfterRefactoring;
    }

    Optional<Change> collect() {
        final IDocument currentlyEditedConfigDocument = getDocumentUnderEdit(redXmlFile);
        if (currentlyEditedConfigDocument == null) {
            return Optional.empty();
        }
        return collectChanges(currentlyEditedConfigDocument);
    }

    private IDocument getDocumentUnderEdit(final IFile redXmlFile) {
        return SwtThread.syncEval(new Evaluation<IDocument>() {

            @Override
            public IDocument runCalculation() {
                final FileEditorInput input = new FileEditorInput(redXmlFile);
                final IEditorPart editor = findEditor(input);

                if (editor instanceof ITextEditor) {
                    final TextEditor ed = ((TextEditor) editor);
                    return ed.getDocumentProvider().getDocument(input);
                }
                return null;
            }
        });
    }

    private static IEditorPart findEditor(final FileEditorInput input) {
        final IEditorReference[] editors = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow()
                .getActivePage()
                .findEditors(input, EditorsUI.DEFAULT_TEXT_EDITOR_ID,
                        IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
        return editors.length > 0 ? editors[0].getEditor(true) : null;
    }

    private Optional<Change> collectChanges(final IDocument document) {
        final RedXmlEditsCollector redXmlEdits = new RedXmlEditsCollector(pathBeforeRefactoring, pathAfterRefactoring);
        final List<TextEdit> validationExcluded = redXmlEdits
                .collectEditsInExcludedPaths(redXmlFile.getProject().getName(), document);
        final List<TextEdit> libraryMoved = redXmlEdits.collectEditsInMovedLibraries(redXmlFile.getProject().getName(),
                document);

        final MultiTextEdit multiTextEdit = new MultiTextEdit();
        for (final TextEdit edit : validationExcluded) {
            multiTextEdit.addChild(edit);
        }
        for (final TextEdit edit : libraryMoved) {
            multiTextEdit.addChild(edit);
        }

        if (multiTextEdit.hasChildren()) {
            final DocumentChange docChange = new DocumentChange(
                    redXmlFile.getName() + " - " + redXmlFile.getParent().getFullPath().toString(), document);
            docChange.setEdit(multiTextEdit);
            docChange.addTextEditGroup(new TextEditGroup("Change paths excluded from validation",
                    validationExcluded.toArray(new TextEdit[0])));
            docChange.addTextEditGroup(
                    new TextEditGroup("Change paths of referenced libraries", libraryMoved.toArray(new TextEdit[0])));
            return Optional.of(docChange);
        } else {
            return Optional.empty();
        }
    }
}
