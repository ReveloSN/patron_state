import type { ReactionStatus } from "../types/reaction";

interface PhaseActionsPanelProps {
  status: ReactionStatus;
  pending: boolean;
  onStart: () => Promise<void>;
  onPause: () => Promise<void>;
  onResume: () => Promise<void>;
  onComplete: () => Promise<void>;
  onCancel: () => Promise<void>;
}

function getAllowedActions(status: ReactionStatus): string[] {
  switch (status) {
    case "Prepared":
      return ["Start the reaction after at least one reactant is registered."];
    case "InProgress":
      return ["Pause execution", "Complete the reaction", "Cancel the reaction"];
    case "Paused":
      return ["Resume execution", "Cancel the reaction"];
    case "Unstable":
      return ["Use the stabilization form with safe conditions", "Cancel the reaction"];
    case "Completed":
    case "Cancelled":
      return ["No further transitions are available."];
    default:
      return [];
  }
}

export function PhaseActionsPanel({
  status,
  pending,
  onStart,
  onPause,
  onResume,
  onComplete,
  onCancel,
}: PhaseActionsPanelProps) {
  const allowedActions = getAllowedActions(status);

  return (
    <section className="panel-section">
      <div className="section-heading">
        <h3>Phase transitions</h3>
        <p>Available state transitions depend on the current lifecycle phase of the reaction.</p>
      </div>

      <ul className="action-list">
        {allowedActions.map((action) => (
          <li key={action}>
            <span>{action}</span>
          </li>
        ))}
      </ul>

      <div className="action-row">
        {status === "Prepared" && (
          <button className="button button--primary" type="button" disabled={pending} onClick={onStart}>
            Start reaction
          </button>
        )}

        {status === "InProgress" && (
          <>
            <button className="button button--secondary" type="button" disabled={pending} onClick={onPause}>
              Pause reaction
            </button>
            <button className="button button--primary" type="button" disabled={pending} onClick={onComplete}>
              Complete reaction
            </button>
            <button className="button button--danger" type="button" disabled={pending} onClick={onCancel}>
              Cancel reaction
            </button>
          </>
        )}

        {status === "Paused" && (
          <>
            <button className="button button--primary" type="button" disabled={pending} onClick={onResume}>
              Resume reaction
            </button>
            <button className="button button--danger" type="button" disabled={pending} onClick={onCancel}>
              Cancel reaction
            </button>
          </>
        )}

        {status === "Unstable" && (
          <button className="button button--danger" type="button" disabled={pending} onClick={onCancel}>
            Cancel reaction
          </button>
        )}
      </div>
    </section>
  );
}
