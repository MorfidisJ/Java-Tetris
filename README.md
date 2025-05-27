# Java Tetris Game
Created by Ioannis Morfidis

## Overview
This is a modern implementation of the classic Tetris game using Java Swing. The game features a clean, modern UI with smooth animations and all the standard Tetris mechanics including piece holding, ghost pieces, and scoring system.

## Features
- 🎮 Classic Tetris gameplay
- 🎨 Modern UI with smooth animations
- 👻 Ghost piece preview
- 💾 Hold piece functionality
- 📊 Score tracking and level progression
- 🎯 Next piece preview
- ⏯️ Pause functionality
- 🎨 Custom block colors and styling

## Game Controls
- **Left Arrow**: Move piece left
- **Right Arrow**: Move piece right
- **Down Arrow**: Soft drop
- **Up Arrow**: Rotate piece
- **Space**: Hard drop
- **C**: Hold piece
- **P**: Pause game

## Game Mechanics

### Scoring System
The scoring system follows the classic Tetris rules:
- 1 line cleared: 100 × level
- 2 lines cleared: 300 × level
- 3 lines cleared: 500 × level
- 4 lines cleared: 800 × level (Tetris)
- Hard drop: 2 points per cell dropped

### Level Progression
- Level increases every 10 lines cleared
- Game speed increases with each level
- Starting speed: 500ms per drop
- Minimum speed: 100ms per drop
- Speed reduction: 50ms per level

## Implementation Details

### Class Structure
```mermaid
classDiagram
    class Tetris {
        -GamePanel gamePanel
        -HoldPanel holdPanel
        -NextPanel nextPanel
        -Timer timer
        +main(String[] args)
        -setupKeyBindings()
        -updateScore(int lines)
        -gameOver()
        -resetGame()
    }
    class GamePanel {
        -Color[][] board
        -Tetromino currentPiece
        -Tetromino nextPiece
        -Tetromino heldPiece
        +moveLeft()
        +moveRight()
        +moveDown()
        +rotate()
        +hardDrop()
        +holdPiece()
    }
    class Tetromino {
        <<abstract>>
        #boolean[][] shape
        #Color color
        #int x, y
        +getType()
        +getRotated()
        +copy()
    }
    Tetris --> GamePanel
    GamePanel --> Tetromino
    Tetromino <|-- TetrominoI
    Tetromino <|-- TetrominoJ
    Tetromino <|-- TetrominoL
    Tetromino <|-- TetrominoO
    Tetromino <|-- TetrominoS
    Tetromino <|-- TetrominoT
    Tetromino <|-- TetrominoZ
```

### Game Flow
```mermaid
flowchart TD
    A[Start Game] --> B[Initialize Board]
    B --> C[Generate First Piece]
    C --> D[Game Loop]
    D --> E{User Input}
    E -->|Move Left| F[Check Collision]
    E -->|Move Right| F
    E -->|Rotate| F
    E -->|Soft Drop| F
    E -->|Hard Drop| G[Drop to Bottom]
    E -->|Hold| H[Swap Pieces]
    F -->|Valid Move| I[Update Position]
    F -->|Invalid Move| D
    I --> J[Check Lines]
    J -->|Lines Found| K[Clear Lines]
    K --> L[Update Score]
    L --> M[Generate New Piece]
    M --> N{Game Over?}
    N -->|Yes| O[End Game]
    N -->|No| D
```

### Piece Types and Rotations
```mermaid
graph TD
    subgraph "Tetromino Types & Rotations"
        direction TB
        
        subgraph "I-Piece (Cyan)"
            I[I-Piece] --> I1[Rotation 0°<br/>****]
            I --> I2[Rotation 90°<br/>*<br/>*<br/>*<br/>*]
            I --> I3[Rotation 180°<br/>****]
            I --> I4[Rotation 270°<br/>*<br/>*<br/>*<br/>*]
        end

        subgraph "J-Piece (Blue)"
            J[J-Piece] --> J1[Rotation 0°<br/>*<br/>***]
            J --> J2[Rotation 90°<br/>**<br/>*<br/>*]
            J --> J3[Rotation 180°<br/>***<br/>  *]
            J --> J4[Rotation 270°<br/> *<br/> *<br/>**]
        end

        subgraph "L-Piece (Orange)"
            L[L-Piece] --> L1[Rotation 0°<br/>  *<br/>***]
            L --> L2[Rotation 90°<br/>*<br/>*<br/>**]
            L --> L3[Rotation 180°<br/>***<br/>*]
            L --> L4[Rotation 270°<br/>**<br/> *<br/> *]
        end

        subgraph "O-Piece (Yellow)"
            O[O-Piece] --> O1[Rotation 0°<br/>**<br/>**]
            O --> O2[Rotation 90°<br/>**<br/>**]
            O --> O3[Rotation 180°<br/>**<br/>**]
            O --> O4[Rotation 270°<br/>**<br/>**]
        end

        subgraph "S-Piece (Green)"
            S[S-Piece] --> S1[Rotation 0°<br/> **<br/>**]
            S --> S2[Rotation 90°<br/>*<br/>**<br/> *]
            S --> S3[Rotation 180°<br/> **<br/>**]
            S --> S4[Rotation 270°<br/>*<br/>**<br/> *]
        end

        subgraph "T-Piece (Purple)"
            T[T-Piece] --> T1[Rotation 0°<br/> *<br/>***]
            T --> T2[Rotation 90°<br/>*<br/>**<br/>*]
            T --> T3[Rotation 180°<br/>***<br/> *]
            T --> T4[Rotation 270°<br/> *<br/>**<br/> *]
        end

        subgraph "Z-Piece (Red)"
            Z[Z-Piece] --> Z1[Rotation 0°<br/>**<br/> **]
            Z --> Z2[Rotation 90°<br/> *<br/>**<br/>*]
            Z --> Z3[Rotation 180°<br/>**<br/> **]
            Z --> Z4[Rotation 270°<br/> *<br/>**<br/>*]
        end
    end

    subgraph "Piece Properties"
        direction LR
        P[Properties] --> P1[I-Piece: 4x1]
        P --> P2[J-Piece: 3x2]
        P --> P3[L-Piece: 3x2]
        P --> P4[O-Piece: 2x2]
        P --> P5[S-Piece: 3x2]
        P --> P6[T-Piece: 3x2]
        P --> P7[Z-Piece: 3x2]
    end

    subgraph "Spawn Positions"
        direction LR
        SP[Spawn] --> SP1[I: (3,0)]
        SP --> SP2[J: (3,0)]
        SP --> SP3[L: (3,0)]
        SP --> SP4[O: (4,0)]
        SP --> SP5[S: (3,0)]
        SP --> SP6[T: (3,0)]
        SP --> SP7[Z: (3,0)]
    end
```

### Piece Statistics
| Piece | Color | Size | Rotations | Spawn Point | Wall Kicks |
|-------|-------|------|-----------|-------------|------------|
| I     | Cyan  | 4x1  | 2         | (3,0)       | 5          |
| J     | Blue  | 3x2  | 4         | (3,0)       | 4          |
| L     | Orange| 3x2  | 4         | (3,0)       | 4          |
| O     | Yellow| 2x2  | 1         | (4,0)       | 1          |
| S     | Green | 3x2  | 2         | (3,0)       | 4          |
| T     | Purple| 3x2  | 4         | (3,0)       | 4          |
| Z     | Red   | 3x2  | 2         | (3,0)       | 4          |

## Technical Details

### Board Dimensions
- Width: 10 cells
- Height: 20 cells
- Block Size: 30 pixels
- Border Width: 5 pixels

### Piece Rotation System
The game implements a wall kick system for piece rotation:
1. Attempts standard rotation
2. If blocked, tries shifting left/right
3. If still blocked, tries shifting up
4. If all attempts fail, rotation is cancelled

### Collision Detection
The game uses a grid-based collision system:
- Checks board boundaries
- Checks for overlapping pieces
- Handles piece placement
- Manages line clearing

## Requirements
- Java Runtime Environment (JRE) 8 or higher
- Java Development Kit (JDK) 8 or higher for development

## How to Run
1. Compile the Java file:
```bash
javac tetris.java
```
2. Run the compiled class:
```bash
java Tetris
```

## Future Improvements
- High score system
- Custom themes
- Sound effects
- Multiplayer support
- Mobile port

## Credits
- Game Design & Implementation: Ioannis Morfidis
- Original Tetris Concept: Alexey Pajitnov
- Java Swing Framework: Oracle Corporation

## License
This project is open source and available under the MIT License.

---
*Created by Ioannis Morfidis* 