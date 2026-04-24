import type {
  AddReactantPayload,
  ApiErrorResponse,
  CreateReactionPayload,
  MeasurementPayload,
  UpdateConditionPayload,
} from "../types/api";
import type { CompoundDetails, CompoundSummary, ReactionRecipe } from "../types/catalog";
import type { Reaction } from "../types/reaction";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "/api";

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(options?.headers ?? {}),
    },
    ...options,
  });

  if (!response.ok) {
    let errorMessage = "The request could not be completed.";

    try {
      const payload = (await response.json()) as ApiErrorResponse;
      errorMessage = payload.message ?? payload.error ?? errorMessage;
    } catch {
      errorMessage = response.statusText || errorMessage;
    }

    throw new Error(errorMessage);
  }

  return (await response.json()) as T;
}

export const reactionApi = {
  createReaction(payload: CreateReactionPayload) {
    return request<Reaction>("/reactions", {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },
  getReactionById(reactionId: string) {
    return request<Reaction>(`/reactions/${reactionId}`);
  },
  addReactant(reactionId: string, payload: AddReactantPayload) {
    return request<Reaction>(`/reactions/${reactionId}/reactants`, {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },
  updateInitialCondition(reactionId: string, payload: UpdateConditionPayload) {
    return request<Reaction>(`/reactions/${reactionId}/conditions`, {
      method: "PUT",
      body: JSON.stringify(payload),
    });
  },
  startReaction(reactionId: string) {
    return request<Reaction>(`/reactions/${reactionId}/start`, {
      method: "POST",
    });
  },
  recordMeasurement(reactionId: string, payload: MeasurementPayload) {
    return request<Reaction>(`/reactions/${reactionId}/measurements`, {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },
  pauseReaction(reactionId: string) {
    return request<Reaction>(`/reactions/${reactionId}/pause`, {
      method: "POST",
    });
  },
  resumeReaction(reactionId: string) {
    return request<Reaction>(`/reactions/${reactionId}/resume`, {
      method: "POST",
    });
  },
  stabilizeReaction(reactionId: string, payload: MeasurementPayload) {
    return request<Reaction>(`/reactions/${reactionId}/stabilize`, {
      method: "POST",
      body: JSON.stringify(payload),
    });
  },
  completeReaction(reactionId: string) {
    return request<Reaction>(`/reactions/${reactionId}/complete`, {
      method: "POST",
    });
  },
  cancelReaction(reactionId: string) {
    return request<Reaction>(`/reactions/${reactionId}/cancel`, {
      method: "POST",
    });
  },
  searchCompounds(query: string) {
    const searchParams = new URLSearchParams({ query });
    return request<CompoundSummary[]>(`/catalog/compounds?${searchParams.toString()}`);
  },
  findCompoundDetails(externalId: string) {
    return request<CompoundDetails>(`/catalog/compounds/${encodeURIComponent(externalId)}`);
  },
  listReactionRecipes() {
    return request<ReactionRecipe[]>("/catalog/recipes");
  },
};
