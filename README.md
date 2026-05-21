# The Java Architect Capstone

## 1. Overview
In this project, your group will act as a **System Design Team**. Instead of writing code first, you will architect a complex text adventure game using **[CommonMark](https://commonmark.org/help/)** (the same syntax used to format this document). Once your blueprint is finalized, you will use a custom **Gemini Gem** to generate the Java source code.

> Before you start designing, play a genre classic for ten minutes to get the feel for what you're building. All four are free to play in your browser on archive.org:
> - **[Zork I](https://archive.org/details/msdos_Zork_I_-_The_Great_Underground_Empire_1980)** (1980, Infocom) — the most famous text adventure, the one that defined the genre. Watch out for the grue.
> - **[Colossal Cave Adventure](https://archive.org/details/msdos_Classic_Adventure_-_The_Original_Colossal_Cave_1996)** (1976, Crowther & Woods) — the original. Predates Zork and established "rooms with exits and items" as the genre's structural language.
> - **[The Hitchhiker's Guide to the Galaxy](https://archive.org/details/msdos_Hitchhikers_Guide_to_the_Galaxy_The_1984)** (1984, Infocom / Douglas Adams) — famously punishing puzzles, but a masterclass in voice and dynamic descriptions that change as the world changes.
> - **[Planetfall](https://archive.org/details/msdos_Planetfall_1983)** (1983, Infocom / Steve Meretzky) — sci-fi adventure with an NPC companion. Shows how a single character can carry a story when you have no graphics to lean on.

**The Challenge:** If your logic is flawed in the blueprint, your game will be broken. Your goal is to design a system robust enough to survive the "vibe coding" process.

### Vibe Coding vs. Agentic Engineering

You may have heard "vibe coding" on social media: prompt an AI, accept whatever it returns, ship anything that runs. The skill you'll practice in this project is the opposite — **agentic engineering**:

| Vibe Coding | Agentic Engineering |
|---|---|
| "Hey AI, make me a game." | "Here is exactly what I want, in writing." |
| Accept whatever the AI returns. | Read what it generated; verify it matches the spec. |
| If it runs, ship it. | If it doesn't match, fix the spec and regenerate. |
| You can't explain it later. | You can defend every line. |
| Fragile — small changes break it. | Robust — same spec → working code, repeatedly. |
| Fast to demo, scary to own. | Slower to start, durable in production. |

Industry is still figuring this out. You'll get a head start.

---

## 2. Logistics

- **Team size:** 4–5 students per group.
- **Suggested roles** (flexible — every member contributes to every phase, these are leadership assignments):
  - **Architect** — drives spec decisions in Sections II / III / IV.
  - **Cartographer** — leads the paper map.
  - **Gem Operator** — handles paste-in / paste-out during Phases B and C.
  - **Scribe** — keeps `debug.log.md` current and accurate.
  - **Presenter** — leads the final walkthrough.

The project moves through three phases — **A: Blueprint** (paper map → spec, no AI), **B: Build** (Gem-generated Java in Code.org), **C: Debug** (spec iteration with three-run verification) — followed by a final presentation. Your instructor will share the schedule for your class.

## 3. Game Design Requirements
Your Game Spec must include the following minimum complexity:
* **The World:** At least **8 unique rooms**.
* **Global State:** At least **3 boolean flags** that track world changes (e.g., `isWifiOn`, `isBookshelfMoved`, `hasEscaped`).
* **Dynamic Descriptions:** Room descriptions must change based on the **Global State**.
* **Interactions:** A "Use [Item] on [Object]" system that triggers state changes.
* **Win/Loss Conditions:** Specific logical triggers that end the game.

---

## 4. Architect vs. Developer

The skills being graded in this project are **design thinking, precision, and iteration** — not Java syntax recall. Your role and the AI's role are split:

| Architect (you) | Developer (the AI) |
|---|---|
| Designs the system | Implements per spec |
| Writes the spec | Translates spec into Java |
| Iterates on ambiguity | Does exactly what it's told |
| Owns the design decisions | Owns the syntax |

The Architect's job is harder than the Developer's, even though it doesn't involve typing semicolons. A vague spec produces a broken game no matter how good the AI is.

---

## 5. Student Workflow

Everything lives in **one Code.org Java Lab project** by the end. You'll create the project up front, fill the spec into a file inside it, then paste the AI-generated Java files alongside the spec. When you're done, you turn in a single Code.org share link that contains the spec, the code, and the debug log.

### Setup (do this once, before Phase A)
1.  In Code.org, create a new Java Lab project named after your game.
2.  Open the **starter Google Drive folder** linked in the Google Classroom assignment. It contains three files: `spec.md`, `Main.java`, and `debug.log.md`. *(The `.log.md` extension keeps the file readable as Markdown while making clear it's an iteration log, not source code.)* (This Google Doc is *not* in the folder — keep it open in a separate tab as your reference.)
3.  In your Code.org project, create three files with those exact names. For each one, open the file from Drive, select all, copy, and paste the contents into the matching Code.org file. (Code.org doesn't let you upload files directly — copy/paste is the workflow.)
4.  Verify all three files are visible in your Code.org project sidebar before continuing.

### Phase A: The Blueprint (No AI)
1.  **Draw a map on paper first.** This is *not* a class diagram — it's a flow chart of your game world. See "Map vs. Class Diagram" below before you start. Sketch every room, label the exits between them, and mark where items and puzzles go. Use multiple sheets of paper and tape them together if you need more space. This is how you catch dead ends, missing connections, and impossible paths *before* you write a line of spec.
2.  **Bring your paper map to an instructor for sign-off.** They will check it against the Map rubric — completeness, reachability, structural soundness. Do not start writing the spec until the map is signed off.
3.  Once the map passes, open `spec.md` **inside your Code.org project** and fill in the CommonMark template — define your Rooms, Items, and Logic Tables. Save often.
4.  Share your Code.org project link with an instructor for the "Logic Audit." **The Logic Audit gates access to the Gem — do not move on to Phase B until the audit passes.**

#### Map vs. Class Diagram

You've drawn UML class diagrams before — boxes for classes, lines for inheritance and association, fields and methods listed inside each box. **The Map is a different kind of diagram.** Section II of your spec already describes the class hierarchy in writing — that's what the LLM will use to generate code. You don't need to draw a class diagram at all; you only need the Map.

| | Class Diagram (what Section II of the spec replaces) | Map (what you're drawing now) |
|---|---|---|
| **Models** | The shape of your code | The shape of your game world |
| **Boxes are** | Classes (`Room`, `Player`, `Item`) | Specific room *instances* (Atrium, Server Room, Hackathon Hall) |
| **Arrows are** | Inheritance, "has-a" relationships | Exits between rooms — directional, sometimes conditional |
| **Closer to a** | UML diagram | Flow chart |
| **Changes when** | You change the class hierarchy | You change the game design |
| **One per project?** | One covers all instances | One Map describes one specific game |

If you *had* drawn a class diagram for this project, it would have ~5 boxes (`Game`, `Room`, `Player`, `Item`, `Food`/`KeyItem`) regardless of whether your game has 8 rooms or 80. The Map has one box per actual room, plus arrows for every exit, plus annotations for items, puzzles, and the state flags that gate doors. Two teams with identical class hierarchies will produce completely different Maps.

The Map is a flow chart with extras:
- **Nodes** are rooms (one box per room, named).
- **Edges** are exits, drawn as directional arrows. Label the direction (N/S/E/W) on the arrow.
- **Conditional edges** — if an exit only opens when a flag flips, draw the arrow dashed and label the condition (e.g., `isWifiOn == true`).
- **Items** are written inside the room where they start.
- **Interactions** are notes on the room where they happen (e.g., "Use Server Key on Server Rack → `isWifiOn = true`").
- **Start and Win** are clearly marked.

If your Map looks like a UML class diagram, you've drawn the wrong thing. If it looks like a subway map with notes scrawled on the stations, you're on the right track.

### Phase B: The Build (AI Implementation)

#### What is a Gem?

A **Gem** is a saved Gemini project with its own reusable instructions. Instead of typing the same long prompt every time you start a chat, you create a Gem once and it carries those instructions into every conversation. A Gem has:

- **Name** — what the Gem is for.
- **Instructions** — the English rules Gemini must follow every time. Think of these as the Gem's *system prompt*: the job description, rules, limits, and response style that stay attached to every chat.
- **Files or context** (optional) — reference material the Gem should use.
- **Preview chat** — a test area where you try prompts and revise the instructions.

A Gem is not magic and it's not a separate person. It's a reusable prompt environment. The **Java Architect Gem** you'll use later in Phase B is only useful because its hidden instructions are strict.

#### B0. Build a Practice Gem: AP CS A Tutor

Before using the Java Architect Gem, build a smaller Gem yourself so you understand what you're working with. This is also the Gem you'll use during Phase C debugging when you have AP CS A questions — it coaches you without writing your project code.

1.  Go to [`gemini.google.com`](https://gemini.google.com).
2.  Open **Explore Gems**, then click **New Gem**.
3.  Name it **AP CS A Tutor**.
4.  Paste the instructions below into the instructions field.
5.  In the preview chat, ask one Java question (e.g., "What's the difference between `==` and `.equals()` for Strings?"). Revise the instructions if the response isn't useful.
6.  Save the Gem.

```commonmark
You are an AP CS A tutor. Help students understand Java concepts without writing
their final answers for them.

Rules:
- Use AP CS A-level Java only.
- Ask one guiding question before giving a direct solution.
- Explain errors using simple language and a small example.
- Use only Java features a first-year AP CS A student has likely learned: classes,
  objects, methods, conditionals, loops, arrays, ArrayLists, Strings, and inheritance.
- If a solution needs a Java feature outside that list, explain the idea in plain
  language first and tell the student to ask the teacher before using it.
- If a student asks for project code, help them reason from their spec instead of
  writing the whole solution.
- Keep responses concise and classroom-appropriate.
```

#### B1. Use the Java Architect Gem

The **Java Architect Gem** is a Gemini AI with strict architecture rules baked in:

- **Five classes minimum:** `Main`, `Game`, `Room`, `Player`, `Item` (plus the subclasses you defined in Section II).
- **AP CS A constructs only** — no language features beyond the AP CS A subset, so the code is something you can read and reason about.
- **JavaDoc on every public method** — the generated code is documented so you can audit it.

These constraints are why Code.org's Java Lab can run the Gem's output without modification. The Gem's full instructions live in `gem.md` — you'll see them after final grades.

Workflow:

1.  Copy the full contents of `spec.md` from your Code.org project.
2.  Paste it into the **Java Architect Gem**.
3.  In your Code.org project, create the Java files the Gem produces (typically `Main.java`, `Game.java`, `Room.java`, `Player.java`, `Item.java`, plus subclasses).
4.  Copy each generated file from the Gem into the matching file in Code.org. Run the project to confirm it compiles.

### Phase C: The Debug (Spec Iteration)

The generated code almost certainly won't work as intended. **You will not edit the Java files.** When the game is broken, your spec is broken. You debug by fixing the spec and re-running the Gem.

> Your Logic Audit-approved spec is a starting point, not a contract. Phase C is *expected* to change the spec — every debug iteration is a spec change. Larger structural changes (adding a room, removing a puzzle, swapping an item) are also fine; just log them in `debug.log.md` so you can show the reasoning at the walkthrough.

The agentic engineering loop:

1.  Run the game in Code.org. Find a bug.
2.  **Read the Gem's Java** to see what your spec actually produced. You're not editing it — you're using it as a diagnostic. The Java is the literal interpretation of your spec; comparing it against your intent is how you find the ambiguity.
3.  Open `spec.md` and figure out what's wrong:
    *   **Spec gap** — a variable, room, exit, item, or interaction the game needs is missing from the spec.
    *   **Spec ambiguity** — the spec says something the Gem reasonably misinterpreted: a vague action, a missing prerequisite, a description that didn't pin down the behavior.
4.  Edit `spec.md` to fix the gap or remove the ambiguity.
5.  Document the bug and your spec fix in `debug.log.md`.
6.  Paste your updated spec back into the Gem. Regenerate every Java file and paste each one into Code.org, overwriting the previous version. The Gem produces slightly different code each run — replace all files, not just the one you think changed.
7.  Run the game again. If new bugs appear, repeat.
8.  **Verify your spec.** Once the game completes the critical path with no bugs, run your final spec through the Gem **three more times**. After each regeneration, paste the new Java files into Code.org and play through the critical path. All three runs must produce code that wins the game. If any run fails, that's a spec ambiguity you missed — fix it and restart verification from run #1. Log all three verification runs in `debug.log.md`.

LLMs are non-deterministic — same spec, different runs, slightly different code. Three consecutive successful regenerations is the bar for "spec is robust."

The skill being graded is **how good your spec gets through iteration**. The three-run verification proves your spec is precise enough that the Gem produces working code reliably, not by luck. If you edit the `.java` files directly, your final spec won't reproduce your submitted code, which is an automatic disqualifier on the rubric.

When you're done, share the final Code.org project link in Google Classroom. That single link is your submission.

---

## 6. Spec Format Guide

The `spec.md` template has six required sections (plus an optional seventh). Here's what good entries look like for each one. Examples below are taken from the Silicon Valley-themed exemplar; your game can be themed however you like.

### Section I: Global Game State
Each flag needs a name, a default value, what triggers it, and what it affects. Think of these as the variables that make your world change.

```commonmark
- `isWifiOn`: Default (False) — Becomes True when 'Server Key' is used on 'Server Rack' in the Server Room. Restores Wi-Fi, the atrium router, and the auditorium Smartboard.
- `hasEscaped`: Default (False) — Win Condition. Becomes True when 'Override USB Drive' is used on 'Security Terminal' in the Main Entrance.
```

You need at least 3 flags plus a win condition. If your game has a loss condition, that's a flag too.

### Section II: Class Hierarchy
At minimum, define the `Item` base class and at least two subclasses with distinct fields. Most games start with this default:

```commonmark
- **Base Class:** `Item` (fields: `String name`, `String description`)
- **Subclass:** `Food` (Extra field: `int healthRestore`)
- **Subclass:** `KeyItem` (Extra field: `String unlocks`)
```

Add or rename subclasses to fit your game (e.g., `Weapon` with `int damage`, `Note` with `String text`). If your game has a boss, NPC, vehicle, or other non-item entity, add a second hierarchy:

```commonmark
- **Base Class:** `Entity` (fields: `String name`, `String description`)
- **Subclass:** `Boss` (Extra fields: `String defeatCondition`, `boolean isActive`)
```

### Section III: Room Definitions
Every room needs a full description, its items, and its exits. If a room's description changes when a state flag changes, write both versions:

```commonmark
### Server Room
- **Description (isWifiOn = False):** "A cramped server room that smells like ozone and despair. The main server rack against the wall has its access panel hanging open — the security key slot is empty. A robot arm prototype leans against the wall."
- **Description (isWifiOn = True):** "The server rack hums quietly. Status LEDs blink in synchronized green. The goated robot arm is still here, leaning against the wall, vibing with electromagnetic energy."
- **Items:** Robot Arm (KeyItem, unlocks "Bookshelf")
- **Exits:**
  - West to Robotics Lab | **Condition:** None
  - East to Secret Vault | **Condition:** `isWifiOn == True`
```

Rooms with only one description just use `**Description:**` without a flag label.

### Section IV: Interaction Logic
Describe every puzzle interaction in plain language. Each one needs the action, where it works, any prerequisites, the state change, and the exact message the player sees:

```commonmark
### Network Restoration
- **Action:** "Use Server Key on Server Rack"
- **Location:** Server Room
- **Prerequisite:** None
- **Effect:** Sets `isWifiOn` = True. Remove Server Key from inventory. Print: "The server rack accepts the key. Status LEDs cycle from red to green. Somewhere in the building, phones erupt with backed-up Slack notifications."
```

Don't write code here — write what happens in English.

### Section V: Critical Path
This is the step-by-step solution to your game. If you can't write this, your puzzle is broken:

```commonmark
1. **Atrium** (start) — Go east.
2. **Cafeteria** — Pick up Server Key. Go west, then south.
3. **Robotics Lab** — Pick up USB-C Cable. Go east.
4. **Server Room** — Use Server Key on Server Rack. (isWifiOn = True) Pick up Robot Arm.
5. **Main Entrance** — Use Override USB Drive on Security Terminal. (hasEscaped = True) You win.
```

If you have a loss condition, write the trap path too.

### Section VI: Item Placement
A quick-reference table. Every item here must appear in exactly one room in Section III:

```commonmark
| Item       | Type         | Room         | Used On         |
|------------|--------------|--------------|-----------------|
| Server Key | KeyItem      | Cafeteria    | Server Rack     |
| Robot Arm  | KeyItem      | Server Room  | Bookshelf       |
| Cold Brew  | Food (15 hp) | Cafeteria    | --              |
```

### Section VII: Turn Mechanics (Optional)
Skip this section if your game has no time-based logic. Use it when you want any of these:

- **NPCs that act on their own** — a creature that moves between rooms, follows the player, or attacks based on game state.
- **Resources that deplete over time** — a torch that burns out, hunger that builds, oxygen that runs low.
- **An inventory cap** — the player can only carry N items.

The Gem implements every mechanic exactly as your spec describes it. If you don't write it here, the Gem won't generate it. Be specific:

```commonmark
### NPCs
- **The Watcher** (Entity subclass): Starts in Library. On each turn, if `isPowerOn == false`, moves one room toward the player. If it reaches the player's room, sets `isCaught = true` (loss condition) and prints "A cold hand closes around your shoulder."

### Resources
- **Torch Fuel** (Player field, starts at 20): Decrements by 1 each turn while `currentRoom.isDark == true`. At 0, sets `isInDark = true` and prints "Your torch sputters and dies."

### Inventory Limit
- Maximum 6 items. Rejection: "Your pack is full. Drop something first."
```

Per-turn logic runs after every successful player command (movement, take, use, etc.) — invalid commands don't tick the clock.

---

## 7. Deliverables
You turn in **two things**:

1.  **Paper Map** — physically signed off by an instructor during Phase A. Keep the signed map in your group folder; it's your proof the Map checkpoint passed.
2.  **Code.org project link** — submitted in Google Classroom at the end of Phase C. The single share link for your project must contain:
    * `spec.md` — your final CommonMark specification.
    * The Java source files (`Main.java`, `Game.java`, `Room.java`, `Player.java`, `Item.java`, plus any subclasses).
    * `debug.log.md` — at least 3 spec iterations (each naming the bug, the classification, and the spec change that fixed it) plus 3 successful verification runs of the final spec.

Everything other than the paper map lives in the Code.org project. One link, one artifact.

---

## 8. Evaluation

Your grade is built from three checkpoints. The first two are **gates** — you can't proceed without passing them — and they're also weighted into your final grade.

| Checkpoint | Weight | Gates |
|---|---|---|
| Map | 20% | Permission to write the spec |
| Spec (Logic Audit) | 50% | Access to the Java Architect Gem |
| Final Presentation | 30% | Final grade |

The Map and Spec checkpoints aren't busywork — they're filters that catch broken designs before AI generation begins.

### The Final Presentation: Code Walkthrough

Your final 30% comes from a **live code walkthrough** where your team explains your project to the class. You will walk through your spec and generated code and answer questions about the decisions you made.

**Format:** One team member shares their screen with the Code.org project open, navigating between `spec.md` and the Java files as the discussion calls for it. The rest of the team narrates, taking turns to cover different sections. Plan in advance who speaks about what — every member should present at least one section.

Be prepared to explain:

* **Architecture:** Why did you define the classes and inheritance hierarchy the way you did? What fields did each subclass add, and why?
* **State Design:** Why did you choose the state flags you did? How do they connect to each other? Could a player get stuck because of a missing flag?
* **Puzzle Logic:** Walk through your critical path. Why did you sequence the puzzles in this order? What alternatives did you consider?
* **Tradeoffs:** What did you change between your first draft and final spec? What broke during testing and what did that teach you about your design?
* **Debugging:** Show your debug log. For each bug, was it a spec gap (something missing) or a spec ambiguity (the Gem made a reasonable but wrong interpretation)? Show the spec change you made to fix it, and explain why your original wording failed.

---

> **Note to Students:** The AI is your junior developer; you are the Senior Architect. It will do exactly what you tell it to do — even if what you told it to do is a bad idea. The spec is the only thing you control: when the code breaks, fix the spec and regenerate. Architects don't reach into the implementation.
