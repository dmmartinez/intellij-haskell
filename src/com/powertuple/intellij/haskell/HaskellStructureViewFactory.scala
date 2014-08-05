/*
 * Copyright 2014 Rik van der Kleij

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.powertuple.intellij.haskell

import javax.swing.Icon

import com.intellij.ide.structureView._
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.pom.Navigatable
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiFile}
import com.powertuple.intellij.haskell.external.GhcModiManager
import com.powertuple.intellij.haskell.psi.impl.HaskellVarImpl
import com.powertuple.intellij.haskell.psi.{HaskellVar, HaskellStartDataDeclaration, HaskellStartDeclarationElement, HaskellStartTypeSignature}
import com.powertuple.intellij.haskell.util.ProjectUtil

class HaskellStructureViewFactory extends PsiStructureViewFactory {
  def getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder = {
    new TreeBasedStructureViewBuilder {
      override def createStructureViewModel(editor: Editor): StructureViewModel = {
        new HaskellStructureViewModel(psiFile)
      }
    }
  }
}

private class HaskellStructureViewModel(psiFile: PsiFile) extends StructureViewModelBase(psiFile, new HaskellStructureViewTreeElement(psiFile, "")) with StructureViewModel.ElementInfoProvider {

  def isAlwaysShowsPlus(structureViewTreeElement: StructureViewTreeElement): Boolean = {
    false
  }

  def isAlwaysLeaf(structureViewTreeElement: StructureViewTreeElement): Boolean = {
    structureViewTreeElement.isInstanceOf[HaskellFile]
  }
}

private class HaskellStructureViewTreeElement(val element: PsiElement, val typeSignature: String) extends StructureViewTreeElement with ItemPresentation {

  def getValue: AnyRef = {
    element
  }

  def navigate(requestFocus: Boolean) {
    element.asInstanceOf[Navigatable].navigate(requestFocus)
  }

  def canNavigate: Boolean = {
    element.asInstanceOf[Navigatable].canNavigate
  }

  def canNavigateToSource: Boolean = {
    element.asInstanceOf[Navigatable].canNavigateToSource
  }

  def getPresentation: ItemPresentation = {
    this
  }

  def getChildren: Array[TreeElement] = {
    import scala.collection.JavaConversions._

    val haskellElements = element match {
      case hf: HaskellFile => PsiTreeUtil.findChildrenOfAnyType(element, classOf[HaskellStartTypeSignature], classOf[HaskellStartDataDeclaration]).toSeq
      case _ => Seq()
    }
    haskellElements.flatMap(sts => createTreeElementWithTypeSignatureFor(sts)).toArray
  }

  private def createTreeElementWithTypeSignatureFor(element: HaskellStartDeclarationElement) = {
    GhcModiManager.getInstance(element.getProject).findInfoFor(element.getContainingFile, element.getIdentifier) match {
      case Some(info) => Some(new HaskellStructureViewTreeElement(element, info.typeSignature))
      case None => if (ProjectUtil.isProjectFile(element.getContainingFile)) None else Some(new HaskellStructureViewTreeElement(element, ""))
    }
  }

  override def getPresentableText: String = {
    element match {
      case hv: HaskellStartDeclarationElement => hv.getIdentifier
      case pf: PsiFile => pf.getName
      case _ => null
    }
  }

  override def getIcon(unused: Boolean): Icon = HaskellIcons.HASKELL_SMALL_LOGO

  override def getLocationString: String = typeSignature
}
