# IntelliJ plugin for Haskell

Some months ago I started his project because I was learning Haskell and I was missing the nice features of IntelliJ IDEA. First approach
 was to use default way of creating IntelliJ plugin by defining grammar and lexer according to
  [Haskell report](http://www.haskell.org/onlinereport/haskell2010/haskellch10.html). That did not workout because I could not define all 
  the recursiveness. Decided to use basic grammar and lexer definitions just for tokenizing Haskell code (e.g. for syntax highlighting). 
  Other Haskell support by help from external tools.

In the meantime also Atsky started to create [Haskell-idea-plugin](https://github.com/Atsky/haskell-idea-plugin) based on ideah plugin in Kotlin. 
 Looking to their recent changes, they are more focusing on Cabal and debugging support.
 
This plugin is written mainly in Scala and is mentioned not to support GHC/Cabal directly. This plugin support sandbox projects
and expects that the Cabal init/install/build is done on command-line. This plugin relies on external tools,
 ghc-mod(i) and haskell-docs, for Haskell language support in IntelliJ IDEA.

# Features
- Syntax highlighting (which can be customized);
- Error/warning highlighting;
- Find Usages of variables/constructors;
- Resolving references of variables (also to library code if library source code is added to project);
- Code completion by resolving references;
- Renaming variables (which first shows preview so refactoring scope can be adjusted);
- View type info;
- View quick documentation;
- View quick definition;
- Code formatting (works reasonably but did not test it with all kinds of code; not finished yet, has to be fine-tuned);
- Structure view;
- Go to declaration (called go to class);
- Go to definition (called go to symbol);
- Code completion by looking to import declarations;

Features are with the help of ghc-mod(i) and haskell-docs!!

# TODO:
- Inspection by hlint;
- Integration of stylish-haskell; 
- Integration of hsimport;
- Smart completion if that is achievable :-) ;
- Hole driven development support;

# Getting started: 
- Install ghc-mod and haskell-docs;
- Set file paths to ghc-modi and haskell-docs in Settings/Haskell;
- Be sure in Settings/Filetypes that Haskell language file is registered with pattern *.hs and Literate Haskell language file with pattern *.lhs; 
- First install Cabal sandbox project (be sure that Haddock documentation is generated, see [haskell-docs](https://github.com/chrisdone/haskell-docs)). 
- After project is installed in sandbox, create Haskell project in IntelliJ by using File/Open from IntelliJ menu;
- Select No SDK in Project Setting;
- Select in Modules Settings which folders to exclude (like .cabal-sandbox and dist) and which folders are Source and Test (normally src and test).
- Add library(package) source root directories to project in Project Settings/Libraries;