import { startTransition, useEffect, useState } from "react";
import { CompoundCatalogPanel } from "../components/catalog/CompoundCatalogPanel";
import { ReactionRecipeCatalogPanel } from "../components/catalog/ReactionRecipeCatalogPanel";
import { AddReactantForm } from "../components/AddReactantForm";
import { CreateReactionForm } from "../components/CreateReactionForm";
import { CurrentConditionsPanel } from "../components/CurrentConditionsPanel";
import { MeasurementForm } from "../components/MeasurementForm";
import { PhaseActionsPanel } from "../components/PhaseActionsPanel";
import { PhaseStatusPanel } from "../components/PhaseStatusPanel";
import { reactionApi } from "../services/reactionApi";
import type { CompoundDetails, CompoundSummary, ReactionRecipe } from "../types/catalog";
import type {
  AddReactantPayload,
  CreateReactionPayload,
  MeasurementPayload,
  UpdateConditionPayload,
} from "../types/api";
import type { Reaction } from "../types/reaction";
import { getStatusMeta } from "../utils/phaseLabels";

function getErrorMessage(error: unknown): string {
  if (error instanceof Error) {
    return error.message;
  }

  return "An unexpected error occurred while communicating with the API.";
}

function formatTimestamp(value: string): string {
  return new Intl.DateTimeFormat(undefined, {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(value));
}

export function ReactionPage() {
  const [reaction, setReaction] = useState<Reaction | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [pending, setPending] = useState(false);
  const [compoundResults, setCompoundResults] = useState<CompoundSummary[]>([]);
  const [selectedCompound, setSelectedCompound] = useState<CompoundDetails | null>(null);
  const [catalogPending, setCatalogPending] = useState(false);
  const [compoundDetailsPending, setCompoundDetailsPending] = useState(false);
  const [catalogError, setCatalogError] = useState<string | null>(null);
  const [recipes, setRecipes] = useState<ReactionRecipe[]>([]);
  const [recipesPending, setRecipesPending] = useState(false);
  const [recipesError, setRecipesError] = useState<string | null>(null);

  const tone = reaction ? getStatusMeta(reaction.status).tone : "prepared";

  async function runRequest(request: () => Promise<Reaction>) {
    setPending(true);
    setError(null);

    try {
      const nextReaction = await request();
      startTransition(() => setReaction(nextReaction));
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    } finally {
      setPending(false);
    }
  }

  async function loadCatalogReferences() {
    setCatalogPending(true);
    setRecipesPending(true);
    setCatalogError(null);
    setRecipesError(null);

    try {
      const [compounds, recipeList] = await Promise.all([
        reactionApi.searchCompounds(""),
        reactionApi.listReactionRecipes(),
      ]);

      startTransition(() => {
        setCompoundResults(compounds);
        setRecipes(recipeList);
      });
    } catch (requestError) {
      const message = getErrorMessage(requestError);
      setCatalogError(message);
      setRecipesError(message);
    } finally {
      setCatalogPending(false);
      setRecipesPending(false);
    }
  }

  useEffect(() => {
    const timerId = window.setTimeout(() => {
      void loadCatalogReferences();
    }, 0);

    return () => window.clearTimeout(timerId);
  }, []);

  async function handleSearchCompounds(query: string) {
    setCatalogPending(true);
    setCatalogError(null);

    try {
      const compounds = await reactionApi.searchCompounds(query);
      startTransition(() => {
        setCompoundResults(compounds);
        setSelectedCompound(null);
      });
    } catch (requestError) {
      setCatalogError(getErrorMessage(requestError));
    } finally {
      setCatalogPending(false);
    }
  }

  async function handleSelectCompound(externalId: string) {
    setCompoundDetailsPending(true);
    setCatalogError(null);

    try {
      const compoundDetails = await reactionApi.findCompoundDetails(externalId);
      startTransition(() => setSelectedCompound(compoundDetails));
    } catch (requestError) {
      setCatalogError(getErrorMessage(requestError));
    } finally {
      setCompoundDetailsPending(false);
    }
  }

  async function handleCreateReaction(payload: CreateReactionPayload) {
    await runRequest(() => reactionApi.createReaction(payload));
  }

  async function handleAddReactant(payload: AddReactantPayload) {
    if (!reaction) {
      return;
    }

    await runRequest(() => reactionApi.addReactant(reaction.id, payload));
  }

  async function handleUpdateCondition(payload: UpdateConditionPayload) {
    if (!reaction) {
      return;
    }

    await runRequest(() => reactionApi.updateInitialCondition(reaction.id, payload));
  }

  async function handleRecordMeasurement(payload: MeasurementPayload) {
    if (!reaction) {
      return;
    }

    await runRequest(() => reactionApi.recordMeasurement(reaction.id, payload));
  }

  async function handleStabilizeReaction(payload: MeasurementPayload) {
    if (!reaction) {
      return;
    }

    await runRequest(() => reactionApi.stabilizeReaction(reaction.id, payload));
  }

  async function handleStartReaction() {
    if (!reaction) {
      return;
    }

    await runRequest(() => reactionApi.startReaction(reaction.id));
  }

  async function handlePauseReaction() {
    if (!reaction) {
      return;
    }

    await runRequest(() => reactionApi.pauseReaction(reaction.id));
  }

  async function handleResumeReaction() {
    if (!reaction) {
      return;
    }

    await runRequest(() => reactionApi.resumeReaction(reaction.id));
  }

  async function handleCompleteReaction() {
    if (!reaction) {
      return;
    }

    await runRequest(() => reactionApi.completeReaction(reaction.id));
  }

  async function handleCancelReaction() {
    if (!reaction) {
      return;
    }

    await runRequest(() => reactionApi.cancelReaction(reaction.id));
  }

  return (
    <div className={`reaction-shell tone-${tone}`}>
      <main className="workspace">
        <aside className="lead-panel">
          <div className="lead-panel__top">
            <span className="eyebrow">Controlled chemical lifecycle</span>
            <h1>State-driven reaction tracking.</h1>
            <p>
              Model a reaction exactly as the laboratory case demands: prepared setup,
              monitored execution, hazardous interruption, stabilization, and final closure.
            </p>

            {reaction ? (
              <PhaseStatusPanel reaction={reaction} />
            ) : (
              <CreateReactionForm pending={pending} onSubmit={handleCreateReaction} />
            )}
          </div>

          <div className="lead-rail">
            <div className="lead-rail__item">
              <span className="lead-rail__index">01</span>
              <div>
                <strong>Prepared</strong>
                <p className="muted-copy">Build the recipe, tune conditions, then begin execution.</p>
              </div>
            </div>
            <div className="lead-rail__item">
              <span className="lead-rail__index">02</span>
              <div>
                <strong>In progress</strong>
                <p className="muted-copy">Record measurements and let safety rules evaluate each change.</p>
              </div>
            </div>
            <div className="lead-rail__item">
              <span className="lead-rail__index">03</span>
              <div>
                <strong>Unstable to closure</strong>
                <p className="muted-copy">Recover with safe values or terminate the lifecycle when necessary.</p>
              </div>
            </div>
          </div>
        </aside>

        <section className="content-panel">
          <header className="toolbar">
            <div className="toolbar__copy">
              <span className="eyebrow">Reaction workspace</span>
              <h2>{reaction ? "Operate the current reaction" : "Create a prepared reaction to begin"}</h2>
              <p className="muted-copy">
                {reaction
                  ? "All commands are routed through the backend domain so each phase enforces its own rules."
                  : "The workspace becomes active after a reaction has been created."}
              </p>
            </div>
          </header>

          {error && <div className="banner">{error}</div>}
          {reaction && <div className="banner banner--info">{reaction.message}</div>}

          {reaction ? (
            <>
              <div className="workspace-grid">
                <div className="workspace-column">
                  <AddReactantForm
                    reactants={reaction.reactants}
                    enabled={reaction.status === "Prepared"}
                    pending={pending}
                    onSubmit={handleAddReactant}
                  />
                  <CurrentConditionsPanel
                    key={`${reaction.id}-${reaction.status}-${reaction.currentCondition.temperatureCelsius}-${reaction.currentCondition.pressureAtm}-${reaction.currentCondition.productConcentrationPercentage}-${reaction.catalyst ?? "none"}`}
                    reaction={reaction}
                    pending={pending}
                    onUpdateCondition={handleUpdateCondition}
                  />
                </div>

                <div className="workspace-column">
                  <MeasurementForm
                    key={`${reaction.id}-${reaction.status}-${reaction.currentCondition.temperatureCelsius}-${reaction.currentCondition.pressureAtm}-${reaction.currentCondition.productConcentrationPercentage}-measurement`}
                    reaction={reaction}
                    pending={pending}
                    onRecordMeasurement={handleRecordMeasurement}
                    onStabilizeReaction={handleStabilizeReaction}
                  />
                  <PhaseActionsPanel
                    status={reaction.status}
                    pending={pending}
                    onStart={handleStartReaction}
                    onPause={handlePauseReaction}
                    onResume={handleResumeReaction}
                    onComplete={handleCompleteReaction}
                    onCancel={handleCancelReaction}
                  />
                </div>
              </div>

              <div className="reference-grid">
                <CompoundCatalogPanel
                  compounds={compoundResults}
                  selectedCompound={selectedCompound}
                  pending={catalogPending}
                  detailsPending={compoundDetailsPending}
                  error={catalogError}
                  onSearch={handleSearchCompounds}
                  onSelectCompound={handleSelectCompound}
                />
                <ReactionRecipeCatalogPanel
                  recipes={recipes}
                  pending={recipesPending}
                  error={recipesError}
                />
              </div>

              <section className="panel-section">
                <div className="section-heading">
                  <h3>Measurement history</h3>
                  <p>Every accepted execution or stabilization reading remains visible in chronological order.</p>
                </div>

                {reaction.measurements.length === 0 ? (
                  <p className="empty-block">No measurements registered yet.</p>
                ) : (
                  <ul className="measurement-list">
                    {[...reaction.measurements].reverse().map((measurement) => (
                      <li key={measurement.recordedAt}>
                        <div className="measurement-meta">
                          <strong>{formatTimestamp(measurement.recordedAt)}</strong>
                          <span>{reaction.status === "Unstable" ? "Latest state may require stabilization." : "Measurement accepted by domain rules."}</span>
                        </div>
                        <div className="measurement-values">
                          <div>
                            <span>Temperature</span>
                            <strong>{measurement.temperatureCelsius.toFixed(1)} deg C</strong>
                          </div>
                          <div>
                            <span>Pressure</span>
                            <strong>{measurement.pressureAtm.toFixed(1)} atm</strong>
                          </div>
                          <div>
                            <span>Concentration</span>
                            <strong>{measurement.productConcentrationPercentage.toFixed(1)}%</strong>
                          </div>
                        </div>
                      </li>
                    ))}
                  </ul>
                )}
              </section>
            </>
          ) : (
            <>
              <section className="panel-section">
                <div className="section-heading">
                  <h3>Expected lifecycle</h3>
                  <p>
                    Create the reaction, add reactants, start execution, register measurements, and let the safety policy drive valid transitions.
                  </p>
                </div>

                <ul className="action-list">
                  <li>
                    <strong>Prepared</strong>
                    <span>Reactants and setup can still change.</span>
                  </li>
                  <li>
                    <strong>InProgress</strong>
                    <span>Measurements are accepted and checked for unsafe thresholds.</span>
                  </li>
                  <li>
                    <strong>Paused / Unstable / Closed</strong>
                    <span>Only the transitions allowed by the current state remain visible.</span>
                  </li>
                </ul>
              </section>

              <div className="reference-grid">
                <CompoundCatalogPanel
                  compounds={compoundResults}
                  selectedCompound={selectedCompound}
                  pending={catalogPending}
                  detailsPending={compoundDetailsPending}
                  error={catalogError}
                  onSearch={handleSearchCompounds}
                  onSelectCompound={handleSelectCompound}
                />
                <ReactionRecipeCatalogPanel
                  recipes={recipes}
                  pending={recipesPending}
                  error={recipesError}
                />
              </div>
            </>
          )}
        </section>
      </main>
    </div>
  );
}
