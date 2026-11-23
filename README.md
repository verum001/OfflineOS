# OfflineOS
## Project Manifest & Development Charter

*Offline from the first boot. Online when you choose.*

*Full control of your Android.*

**Project status:** Concept and initial application-development phase

**License:** Apache License 2.0 (preferred for original code; third-party components retain their respective licenses)

**Development model:** Open source, AI-assisted, community-oriented

---

## 1. Project Vision

OfflineOS is an initiative to develop a lightweight, transparent, privacy-respecting Android ecosystem in which users control their applications, network activity, and personal data.

Modern smartphones frequently include unnecessary software, background services, advertising components, telemetry, and applications that communicate with remote servers without providing an obvious benefit to the user.

OfflineOS aims to reverse this model.

The operating system itself should not require an internet connection to function. Applications that genuinely require connectivity, such as Telegram or a web browser, should work normally without complicated configuration.

OfflineOS is not intended to eliminate the internet. It is intended to eliminate unnecessary dependence on it.

**Fundamental principle:**

*An application should do exactly what its user expects — nothing unnecessary, nothing hidden.*

## 2. Core Philosophy

### 2.1 Offline by Default

The operating system must be usable immediately after installation without requiring:

- Internet connectivity.
- Google or manufacturer accounts.
- Cloud registration.
- Mandatory synchronization.
- Unnecessary setup procedures.

The long-term objective is zero unsolicited internet connections initiated by the operating system after its first boot.

This must be validated through repeatable network testing rather than assumed from the absence of Google applications.

### 2.2 Online When Necessary

OfflineOS must preserve normal internet functionality.

For example:

- A calculator requires no network connectivity.
- A calendar should function completely offline.
- A local gallery should not communicate with external servers.
- Telegram should be able to access the internet and retain its essential functionality.
- A browser must be able to access websites normally.

Network access should follow transparent policies without imposing unnecessary setup steps on users.

Third-party APKs must not be automatically trusted merely because they request internet access. The initial policy should distinguish verified network-dependent applications from unknown applications without creating burdensome configuration.

### 2.3 Minimalism

Software should contain only the functionality required for its intended purpose.

Avoid:

- Advertising SDKs.
- Unnecessary analytics and tracking.
- Redundant dependencies.
- Background services without a clear purpose.
- Unnecessary permissions.
- Artificial account requirements.
- Unnecessary visual complexity.

Application size is an optimization goal, not an absolute requirement. Correctness, security, compatibility, and maintainability take priority.

### 2.4 User Control

Users should be able to understand:

- Which applications have internet access.
- Which applications are currently communicating.
- How much data each application sends and receives.
- Which network destinations are contacted, where technically observable.
- Which permissions applications possess.
- Which components are installed on the device.

The system should make these details accessible without requiring advanced Android knowledge.

### 2.5 Privacy Without Sacrificing Functionality

Privacy protections must not unnecessarily prevent users from running useful software.

The project should preserve compatibility with ordinary Android APK files.

Optional compatibility components, including microG or other independently implemented APIs, may be considered when required.

Dependencies on proprietary services must be clearly documented.

### 2.6 Open Source and Verifiability

Original OfflineOS components should be publicly available under an open-source license.

The project should aim for:

- Public source repositories.
- Documented build instructions.
- Reproducible builds where practical.
- Published dependency information.
- Permission audits.
- Automated testing.
- Transparent security reporting.

Claims such as "no tracking" or "no network activity" must be supported by evidence.

---

## 3. Development Strategy

OfflineOS will develop through two independent but compatible directions.

### Track A — OfflineOS Apps

**Immediate priority.**

Develop independent, lightweight Android applications that work on ordinary Android devices without root access or custom firmware.

Potential applications include:

| Application | Purpose | Network requirement |
|---|---|---|
| Little Calculator | Lightweight scientific and standard calculator | None |
| Little Calendar | Minimal personal calendar | None |
| Little Notes | Private local notes | None |
| Little Gallery | Local photo and video gallery | None |
| Little Recorder | Audio recording | None |
| Little Files | File management | None |
| Little Player | Local media playback | None |
| Little Launcher | Minimal Android home screen | None |
| Little Monitor | Network activity monitoring | Depends on implementation |
| Little Firewall | Per-application network control | Local VPN or elevated system privileges |
| Little  Agent | Offline knowledge retrieval and local AI | None for core offline functionality |

Names are given for exemple.
These applications must be individually installable and must not require the entire OfflineOS ecosystem.

Existing projects, such as Pretty Calendar and DataEater, are separate projects that can contribute to this ecosystem without requiring immediate restructuring.

### Track B — OfflineOS System

**Long-term optional objective.**

Evaluate whether a complete custom Android distribution is justified after developing and testing the applications.

If justified, base the system on AOSP, LineageOS, or another suitable open-source Android distribution.

Do not implement Android from scratch.

Preserve:

- Android APK compatibility.
- Android Runtime (ART).
- Essential Android security mechanisms.
- Application sandboxing.
- Verified boot where supported.
- Encryption and secure storage.
- Hardware compatibility.
- Security update capability.

Avoid unnecessary Google dependencies, manufacturer applications, unsolicited network activity, and redundant software.

A custom ROM must not be pursued merely for branding. It should address demonstrable limitations that cannot reasonably be solved through existing distributions or ordinary applications.

---

## 4. Network Architecture Principles

The future system should distinguish three categories of software.

**Category A — Pure Offline**

Applications that do not need internet access.

They must not request the Android INTERNET permission and must not depend on network services.

Examples: calculator, notes, gallery, offline calendar.

**Category B — Network-Dependent**

Applications that require internet connectivity for their stated purpose.

Examples: Telegram, browser, email client.

They should work normally while remaining subject to transparent monitoring and user-configurable policies.

**Category C — System Components**

Android framework services, update mechanisms, connectivity checks, DNS, time synchronization, and other operating-system functions.

Each component must be investigated and documented.

Unnecessary system-originated network traffic should be eliminated. Essential or explicitly enabled traffic must be understandable and controllable where technically possible.

The project must recognize the limits of VPN-based filtering, shared system UIDs, encrypted network traffic, and proprietary hardware firmware.

---

## 5. Offline First Standard (OFS)

OfflineOS should develop a verifiable standard for lightweight, privacy-respecting Android applications.

The proposed classifications are:

**Pure Offline:** No direct internet permission, no network-dependent core functionality, and no advertising or tracking SDKs.

**Network Transparent:** Network access is justified, documented, and restricted to declared purposes.

**Audited:** Source code, dependencies, permissions, build process, and observable runtime behavior have been examined using a published methodology.

These classifications are independent attributes rather than a mandatory progression.

No application may claim certification until an actual verification process exists and the relevant checks have been completed.

The standard should eventually support automated APK analysis and human-readable audit reports.

---

## 6. Development Technology

Recommended initial approach:

- Kotlin or Java for Android applications.
- Native Android APIs where practical.
- Minimal third-party dependencies.
- Android Studio and Gradle-compatible builds.
- Git and GitHub for version control.
- GitHub Actions or equivalent CI.
- Emulator-based automated testing.
- Real-device testing before public release.

Choose technologies individually for each application.

Do not use large UI frameworks merely for development convenience when a simpler implementation adequately meets the requirements.

Do not compromise Android security features to reduce APK size.

---

## 7. AI-Assisted Development

The project is intended to be manageable by an individual project owner using ChatGPT Work, Codex, and other AI-assisted development tools.

The human project owner defines objectives, approves decisions, and determines release readiness.

AI agents may perform specialized tasks:

- Architecture and research.
- Android implementation.
- User interface design.
- Performance and size optimization.
- Automated testing.
- Security auditing.
- Documentation.
- Release preparation.

Independent agents should use isolated Git branches or worktrees where appropriate.

Code must not be merged solely because an AI agent reports success.

Critical changes must pass automated tests and, where necessary, independent review.

The project should remain maintainable even if the AI development tools change.

---

## 8. Security Requirements

Security takes precedence over aggressive minimization.

Mandatory principles:

1. Follow Android application sandboxing and permission rules.
2. Avoid unnecessary privileged permissions.
3. Keep dependencies updated and review known vulnerabilities.
4. Do not embed private signing keys or credentials in repositories.
5. Validate untrusted files and imported data.
6. Implement secure backup and recovery mechanisms where appropriate.
7. Publish accurate limitations and test results.
8. Protect user data during updates and migrations.
9. Do not introduce hidden remote-control or telemetry functionality.
10. Do not claim that open source alone guarantees security.

A future operating-system distribution must maintain reliable security updates and device-specific security testing.

---

## 9. User Experience

OfflineOS applications should share a recognizable design philosophy.

**Visual priorities:**

- Clean and minimal.
- Fast and responsive.
- Comfortable on small screens.
- Readable typography.
- Consistent interaction patterns.
- No unnecessary onboarding.
- No distracting animations.
- Support for light and dark modes where appropriate.

Applications should launch directly into their primary functionality whenever possible.

Consistency is desirable, but individual application needs take priority over rigid visual uniformity.

Accessibility must not be sacrificed for minimalism.

---

## 10. Initial Roadmap

### Phase 1 — Project Foundation

- Publish the project vision.
- Establish coding and licensing conventions.
- Define the initial Offline First Standard.
- Create a reusable Android development checklist.
- Set up automated build and testing workflows.

### Phase 2 — Lightweight Applications

- Review Pretty Calendar against project principles.
- Develop a minimal calculator as a reference application.
- Develop lightweight notes or another useful offline utility.
- Publish source code and release APKs.
- Record binary sizes, permissions, and performance results.

### Phase 3 — Network Transparency

- Research Android network monitoring capabilities.
- Prototype Little Monitor without root.
- Document limitations of Android VPN and traffic accounting APIs.
- Evaluate root-enabled functionality separately.
- Develop repeatable network testing procedures.

### Phase 4 — Ecosystem Integration

- Establish shared design guidelines.
- Create a lightweight launcher if justified.
- Build a project website or application catalog.
- Document compatibility with stock Android, LineageOS, and GrapheneOS.

### Phase 5 — Feasibility of a Custom OS

- Compare existing Android distributions against OfflineOS requirements.
- Identify demonstrable unmet needs.
- Evaluate suitable hardware.
- Prototype only if a dedicated operating system offers sufficient benefit.

**Current project priority: Phases 1 and 2.**

Do not initiate custom ROM development without explicit approval.

---

## 11. Success Criteria

Early progress will be measured through practical outcomes:

- Applications provide their core functionality without unnecessary network access.
- APK size and resource consumption remain reasonable.
- Apps operate on a documented range of Android devices.
- No hidden telemetry or advertising dependencies are introduced.
- Users can independently install, remove, and update each application.
- Source code and building instructions are publicly available.
- Security and functionality are tested before release.

Long-term success is not measured by the number of applications or the existence of a custom ROM.

It is measured by how much useful functionality users obtain with minimal software, minimal dependencies, and meaningful control.

---

## 12. Instructions for ChatGPT Work

Treat this manifest as the authoritative initial product vision for OfflineOS.

Your first assignment is to analyze the feasibility of the OfflineOS Apps initiative, not to build a complete operating system.

Proceed as follows:

1. Review the goals, identify technical contradictions, ambiguities, and unrealistic assumptions.
2. Research suitable Android architectures, development practices, and existing open-source components that could reduce unnecessary work.
3. Propose a minimal repository structure and development workflow.
4. Define objective, testable requirements for Pure Offline applications.
5. Develop a practical roadmap for the first three independent applications.
6. Recommend a small AI-assisted development workflow with independent testing and review.
7. Estimate development difficulty, maintenance burden, and key security risks.
8. Clearly separate verified facts, assumptions, and recommendations.

Do not begin implementing a custom Android firmware.

Do not modify existing repositories or projects without identifying the intended repository and obtaining approval.

Do not replace existing projects such as DataEater or Pretty Calendar without explicit instructions.

Present findings in accessible language suitable for a project owner who does not have formal software engineering training.

Prioritize simplicity, security, practical usefulness, and maintainability over technical novelty.

---

## Final Statement

OfflineOS is not about rejecting modern technology.

It is about restoring simplicity, transparency, and ownership to personal computing.

A smartphone should perform the tasks its owner chooses, without unnecessary software, hidden activity, or forced dependence on remote services.

**Offline from the first boot. Online when you choose.**

**OfflineOS — Your phone. Your rules.**


## Applications

- [Little Calculator](Apps/LittleCalculator): offline calculator with history and unit conversions. [Download test APK](Apps/LittleCalculator/downloads/LittleCalculator-0.1.0-test.apk?raw=true). Early test build; device testing pending.
