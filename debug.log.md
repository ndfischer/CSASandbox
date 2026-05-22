# Debug Log — Gold Rush California
**Nico Fischer, Carlos Guo, Ivy Bock, Ved Vasudev Jayaram**

Every change to `spec.md` after the Logic Audit is recorded here. Each iteration names the bug observed when running the generated game, classifies it, and describes the spec change that fixed it.

**Classifications:**
- **Spec gap** — the spec was missing something the game needed.
- **Spec ambiguity** — the spec said something the Gem reasonably misinterpreted.

---

## Iterations

### Iteration 1 — Inconsistent Java file structure across Gem runs

**Bug observed when running the game:** When we regenerated the project, the Gem produced a different file structure each run. One run put every class inside `Main.java`; another split them into separate files but with inconsistent names. This made it impossible to keep multiple versions side-by-side in our GitHub repo, since the file set kept changing.

**Classification:** Spec gap.

**Why the original spec wording failed:** Our spec defined the class hierarchy in Section II but never told the Gem how to lay those classes out as Java files. With no instruction either way, the Gem made a reasonable choice, but a different reasonable choice on each run.

**Spec change:** Added an "Output Files" section between the intro and Section I, listing the 12 Java files the Gem must produce: `Main.java`, `Game.java`, `Room.java`, `Player.java`, `Item.java`, `Ore.java`, `Gold.java`, `Coal.java`, `Crop.java`, `Wheat.java`, `Livestock.java`, `Cattle.java`.

---

### Iteration 2 — Playtesters couldn't figure out what to do

**Bug observed when running the game:** Playtesters loaded the game and got stuck almost immediately. They couldn't tell what verbs to type, for example, at the American River they read "specks of yellow shimmer in the riverbed" but had no idea the verb was `pan`. Even when they typed something reasonable, the Gem's parser handled unrecognized input inconsistently across runs (sometimes ignoring it silently, sometimes crashing).

**Classification:** Spec ambiguity.

**Why the original spec wording failed:** Section IV described each action in plain language but never declared the full set of accepted verbs, and Section III's room descriptions named items and exits but didn't telegraph which verbs would work on them. The Gem reasonably implemented each action exactly as written, leaving the player to guess.

**Spec change (two parts):**
1. Added a "Command Vocabulary" section listing every accepted verb, where each works, and the fallback message for unrecognized input. Included `help`, `look`, and `inventory` as standard convenience commands.
2. Edited room descriptions in Section III to weave hints about the available actions into the prose (e.g. the American River description now ends with "you could pan here"; Brannan's Store lists what can be bought and sold). Sacramento was left unchanged because it's a pure crossroads with no actions.

---

### Iteration 3 

---

## Verification Runs

Once the game completes the critical path successfully, the final spec is run through the Gem three more times. All three runs must produce code that wins the game. If any run fails, the spec has a remaining ambiguity — fix it and restart verification from Run 1.

### Run 1

**Date:**
**Outcome:** Pass / Fail
**Notes:**

### Run 2

**Date:**
**Outcome:** Pass / Fail
**Notes:**

### Run 3

**Date:**
**Outcome:** Pass / Fail
**Notes:**
