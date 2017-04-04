package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.swing.text.Position;
import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        Job someJob = jobData.findById(id);
        model.addAttribute("job", someJob);
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        if (errors.hasErrors()) {
            model.addAttribute(jobForm);
            return "new-job";
        } else {

            // create a new Job
            Employer newEmployer = jobData.getEmployers().findById(jobForm.getEmployerId());
            Location newLocation = jobForm.getLocation();
            PositionType newPositionType = jobForm.getPositionType();
            CoreCompetency newCoreCompetency = jobForm.getCoreCompetency();

            Job newJob = new Job(jobForm.getName(), newEmployer, newLocation, newPositionType, newCoreCompetency);

            // add newJob to jobData store
            jobData.add(newJob);

            // add it to the model for display and redirect
            model.addAttribute("job", newJob);
            model.addAttribute("id", newJob.getId());
            // return "job-detail"; //this currently worked but didn't redirect to /job?id=X
            return "redirect:job-detail";
        }
    }
}
