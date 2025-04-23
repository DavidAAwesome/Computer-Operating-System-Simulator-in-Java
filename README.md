# Computer-Operating-System-Simulator-in-Java

This project simulates a simple operating system written entirely in Java. It models key OS functionalities including process scheduling, memory management, inter-process communication, device I/O, and a virtual file system. The system is designed to demonstrate how these core components interact and operate within an educational or simulated environment.

## 🧠 Features

### ✅ Process Management
- Multilevel priority queues (`RealTime`, `Interactive`, `Background`)
- Time-sliced scheduling with process preemption
- Cooperative multitasking (`cooperate()` call)
- Support for process sleep and termination
- Kernel-mode syscall simulation

### 🧵 Userland Processes
- Extendable by implementing the `UserlandProcess` class
- Includes example processes like `HelloWorld`, `GoodbyeWorld`, and memory stress tests

### 💾 Virtual Memory
- Page-based virtual memory with a custom page table per process
- Swap file for backing memory (simulated with file I/O)
- Basic TLB (Translation Lookaside Buffer) simulation
- Support for page faults and memory allocation/freeing

### 📤 Inter-Process Communication
- Send and receive `KernelMessage` objects by PID
- Message queue system per process
- Blocking `WaitForMessage()` syscall

### 📁 File and Device I/O
- Virtual File System (`VFS`) that abstracts device interactions
- `RandomDevice` (generates random data)
- `FakeFileSystem` (file-backed byte access)
- Unified `Device` interface for pluggable devices

## 🏁 Getting Started

### Requirements
- Java 8 or later
- IDE or CLI to compile and run Java programs

### Running the OS
Compile and run the entry point:

```bash
javac Main.java
java Main
