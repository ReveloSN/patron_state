import type { ReactionStatus } from "../types/reaction";

interface StatusMeta {
  label: string;
  detail: string;
  tone: "prepared" | "active" | "paused" | "unstable" | "finished" | "aborted";
}

const STATUS_META: Record<ReactionStatus, StatusMeta> = {
  Prepared: {
    label: "Prepared",
    detail: "Setup mode. Reactants and initial conditions can still change.",
    tone: "prepared",
  },
  InProgress: {
    label: "In progress",
    detail: "Execution mode. Measurements are accepted while the reaction remains safe.",
    tone: "active",
  },
  Paused: {
    label: "Paused",
    detail: "Execution is temporarily stopped. Resume or cancel to continue.",
    tone: "paused",
  },
  Unstable: {
    label: "Unstable",
    detail: "Unsafe conditions detected. Normal measurements are blocked until stabilization.",
    tone: "unstable",
  },
  Completed: {
    label: "Completed",
    detail: "Successful finish. Only the final summary remains available.",
    tone: "finished",
  },
  Cancelled: {
    label: "Cancelled",
    detail: "Execution was cancelled. Registered information remains available for review.",
    tone: "aborted",
  },
};

export function getStatusMeta(status: ReactionStatus): StatusMeta {
  return STATUS_META[status];
}
